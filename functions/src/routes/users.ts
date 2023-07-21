import { db } from "../config/firebase";
import { Request, Response } from "express";

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
  console.log("request body" + JSON.stringify(req.body))

  db.doc(`/users/${userId}`)
    .update({ "bio": req.body["bio"] })
    .then((doc) => {
      return res.status(200).json({ success: true })
    })
    .catch((err) => {
      console.error(err);
      return res.status(500).json({ err });
    })
}
