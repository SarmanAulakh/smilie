import { db } from "../config/firebase";
import { Request, Response } from "express";

export function getUserDetails(req: Request, res: Response) {
  let userId = req.params.userId;
  console.log("request body" + JSON.stringify(req))

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
  // Documentation found here https://dev.to/lucidmach/the-20-firebase-that-ll-do-80-of-the-task-a-firestore-cheatsheet-304p
  let userId = req.params.userId;
  console.log("request body" + JSON.stringify(req.body))

  db.collection("users").doc(userId)
    .update({ "bio": req.body["bio"] })
    .then((doc) => {
      return res.status(200).json({ success: true })
    })
    .catch((err) => {
      console.error(err);
      return res.status(500).json({ err });
    })
}

export function getUserMetrics(req: Request, res: Response) {
  let userId = req.params.userId;

  db.doc(`/users/${userId}`)
    .collection(`metrics`)
    .get()
    .then((doc) => {
      let data:Metric[] = new Array(doc.size);
      for(var i in doc.docs) {
        data[i].id = doc.docs[i].id;
        data[i].name = doc.docs[i].data().name;
        data[i].public = doc.docs[i].data().public;
        data[i].active = doc.docs[i].data().active;
      }
      
      return res.status(200).json({ data });
    })
    .catch((err) => {
      console.error(err);
      return res.status(500).json({ err });
    })
}

export function updateUserMetrics(req: Request, res: Response) {
  let userId = req.params.userId;
  
  for(var metric of req.body) {
    if(metric.hasOwnProperty("metricId")) {
      let metricId = metric["metricId"]
      db.doc(`/users/${userId}`)
        .collection(`metrics`)
        .doc(`/${metricId}`)
        .update({active: metric["active"]})
        .then((doc) => {
          return res.status(200).json({ success: true })
        })
        .catch((err) => {
          console.error(err);
          return res.status(500).json({ err });
        })
    } else {
      db.doc(`/users/${userId}`)
        .collection(`metrics`)
        .add({name: metric["name"],
              public: metric["public"],
              active: metric["active"],
              values: metric["values"]
        })
        .then((doc) => {
          return res.status(200).json({ req })
        })
        .catch((err) => {
          console.error(err);
          return res.status(500).json({ err });
        })
    }
  }
  
  
}

export function addMetricEntry(req: Request, res: Response) {
  let userId = req.params.userId;
  let metricId = req.params.metricId;

  db.doc(`/users/${userId}`)
    .collection(`metrics`)
    .doc(`/${metricId}`)
    .collection(`values`)
    .add({value: req.body["value"],
          date: req.body["date"],
          weight: req.body["weight"]
    
    })
    .then((doc) => {
      return res.status(200).json({ req })
    })
    .catch((err) => {
      console.error(err);
      return res.status(500).json({ err });
    })
}

interface Metric{
  id: String,
  name: String,
  public: Boolean,
  active: Boolean,
  values: [Value]
}

interface Value{
  value: Number,
  date: DateConstructor,
  weight: Number
}

