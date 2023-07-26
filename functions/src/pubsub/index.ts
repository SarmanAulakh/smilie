import * as functions from "firebase-functions";
import { db, messanger } from "../config/firebase";
import { MulticastMessage } from "firebase-admin/lib/messaging/messaging-api";
import { FieldValue } from "firebase-admin/firestore";

// FE: has the user data
// user "a" has updated their metrics: then the user can view their profile (graph or whatever)
// has the followers id

export interface DayRatedData {
  type: "dayRated";
  userId: string;
  username: string;
  date: string;

  followers: string[]; // uid[]
}

export interface FeedItem {
  message: string;
  senderId: string;
}

exports.insertFromPubsub = functions.pubsub
  .topic("dayRated")
  .onPublish((message, context) => {
    const data = message.json as DayRatedData;
    const notificationBody = "A new rating was added by " + data.username;

    // add to followers feed
    db.collection("users")
      .doc(data.userId)
      .update({
        feed: FieldValue.arrayUnion({
          senderId: data.userId,
          message: notificationBody,
        } as FeedItem),
      });

    // create notification
    db.collection("notifications")
      .doc()
      .set(data)
      .then(() => {
        // notify all friends that rating was added
        const followers = data.followers;
        for (const follower of followers) {
          db.collection("users")
            .doc(follower)
            .get()
            .then((doc) => {
              if (doc.exists) {
                const data = doc.data();
                const payload: MulticastMessage = {
                  tokens: data ? data.registrationTokens : [], // https://firebase.google.com/docs/cloud-messaging/send-message
                  notification: {
                    title: "New Rating",
                    body: notificationBody,
                  },
                };
                messanger
                  .sendEachForMulticast(payload)
                  .then((response) => {
                    console.log("Successfully sent message:", response);
                  })
                  .catch((error) => {
                    console.log("Error sending message:", error);
                  });

                // Have to add code to make notification appear in home screen
              }
            })
            .catch((error) => {
              console.log("Error adding friend notif to database", error);
            });
        }
      })
      .catch((error) => {
        console.log("Error adding rating to database", error);
      });
  });
