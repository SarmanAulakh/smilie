import { FieldValue } from "firebase-admin/firestore";
import { db } from "../config/firebase";
import { Request, Response } from "express";

enum UserTypes {
  DEFAULT = "DEFAULT",
  STUDENT = "STUDENT",
  INFLUENCER = "INFLUENCER",
}

export function createNewUser(req: Request, res: Response) {
  const user = req.body;
  const userType: UserTypes = user.userType;

  user.show_notifications = true;

  const batch = db.batch();

  console.log(userType, userType === UserTypes.STUDENT);

  let metrics: Metric[] = [
    { name: "Amount of Sleep", public: true, active: true, values: [] },
    { name: "Quality of Sleep", public: true, active: true, values: [] },
    { name: "Exercise", public: true, active: true, values: [] },
    { name: "Food", public: true, active: true, values: [] },
    { name: "Overall", public: true, active: true, values: [] },
  ];

  switch (userType) {
    case UserTypes.STUDENT:
      metrics = metrics.concat([
        { name: "Productivity (School)", public: true, active: true, values: [] },
        { name: "Video Games", public: false, active: true, values: [] },
        { name: "Time spent on Assignments", public: true, active: true, values: [] },
      ]);
      break;
    case UserTypes.INFLUENCER:
      metrics = metrics.concat([
        { name: "Time on Phone", public: true, active: true, values: [] },
      ]);
      break;
    default:
      break;
  }

  db.collection("users")
    .doc(user.id)
    .set(user)
    .then(() => {
      const collectionRef = db.doc(`/users/${user.id}`).collection(`metrics`);

      metrics.forEach((data) => {
        const docRef = collectionRef.doc();
        batch.set(docRef, data);
      });

      batch
        .commit()
        .then(() => {
          console.log("bastched")
          return res.status(200).json({ success: true });
        })
        .catch((err) => {
          return res.status(500).json({ err });
        });
    })
    .catch((err) => {
      console.error(err);
      return res.status(500).json({ err });
    });
}

export function getUserDetails(req: Request, res: Response) {
  let userId = req.params.userId;

  db.doc(`/users/${userId}`)
    .get()
    .then((doc) => {
      if (doc.exists) {
        return res.status(200).send(doc.data());
      } else {
        return res.status(404).json({ error: "User not found" });
      }
    })
    .catch((err) => {
      console.error(err);
      return res.status(500).json({ err });
    });
}

export function addUserDetails(req: Request, res: Response) {
  let userId = req.params.userId;
  const { email, bio, show_notifications } = req.body

  db.collection("users")
    .doc(userId)
    .update({ 
      email,
      bio,
      show_notifications,
    })
    .then((_) => {
      return res.status(200).json({ success: true });
    })
    .catch((err) => {
      console.error(err);
      return res.status(500).json({ err });
    });
}

export function getUserMetrics(req: Request, res: Response) {
  let userId = req.params.userId;

  db.collection(`users`)
    .doc(userId)
    .collection(`metrics`)
    .get()
    .then((doc) => {
      // console.log("metric doc:"+JSON.stringify(doc))
      // console.log("metric docs:"+JSON.stringify(doc.size))

      let data: Metric[] = new Array(doc.size);
      for (var i in doc.docs) {
        data[i] = {
          id: doc.docs[i].id,
          name: doc.docs[i].data().name,
          public: doc.docs[i].data().public,
          active: doc.docs[i].data().active,
          values: doc.docs[i].data().values,
        };
      }
      //console.log("metric data:"+JSON.stringify(data))
      return res.status(200).json(data);
    })
    .catch((err) => {
      console.error(err);
      return res.status(500).json({ err });
    });
}

export function updateUserMetrics(req: Request, res: Response) {
  let userId = req.params.userId;
  for (var metric of req.body) {
    if (metric.hasOwnProperty("id") && metric["id"] != null) {
      let metricId = metric["id"];
      db.doc(`/users/${userId}`)
        .collection(`metrics`)
        .doc(`/${metricId}`)
        .update({ active: metric["active"], public: metric["public"] })
        .catch((err) => {
          console.error(err);
          return res.status(500).json({ err });
        });
    } else {
      db.doc(`/users/${userId}`)
        .collection(`metrics`)
        .add({
          name: metric["name"],
          public: metric["public"],
          active: metric["active"],
          values: metric["values"],
        })
        .catch((err) => {
          console.error(err);
          return res.status(500).json({ err });
        });
    }
  }
  return res.status(200).json(true);
}

export function addMetricEntry(req: Request, res: Response) {
  let userId = req.params.userId;
  let metricId = req.params.metricId;

  //console.log(req.body["value"])
  db.doc(`/users/${userId}`)
    .collection(`metrics`)
    .doc(`/${metricId}`)
    .update({
      values: FieldValue.arrayUnion({
        value: req.body["value"],
        date: req.body["date"],
        weight: req.body["weight"],
      }),
    })
    .then((doc) => {
      return res.status(200).json(true);
    })
    .catch((err) => {
      console.error(err);
      return res.status(500).json({ err });
    });
}

interface Metric {
  id?: String;
  name: String;
  public: Boolean;
  active: Boolean;
  values: Array<Value>;
}

interface Value {
  value: Number;
  date: String;
  weight: Number;
}
