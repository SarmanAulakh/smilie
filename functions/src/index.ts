/**
 * Import function triggers from their respective submodules:
 *
 * import {onCall} from "firebase-functions/v2/https";
 * import {onDocumentWritten} from "firebase-functions/v2/firestore";
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

import {onRequest} from "firebase-functions/v2/https";
import * as logger from "firebase-functions/logger";
// import * as functions from "firebase-functions";

// Start writing functions
// https://firebase.google.com/docs/functions/typescript

const functions = require('firebase-functions'); //59K (gzipped: 17.9K)
const admin = require('firebase-admin');
admin.initializeApp();

exports.insertFromPubsub = functions.pubsub.topic('dayRated').onPublish((message, context) => {
    const data = message.json;
    const db = admin.firestore();
    const docRef = db.collection('dayRated').doc(data.date);
    return docRef.set(data).then(() => {
        console.log('New rating added to database');
        // notify all friends that rating was added
        const followers = data.followers;
        for (const follower of followers) {
            const docRef = db.collection('users').doc(follower);
            docRef.get().then((doc) => {
                if (doc.exists) {
                    const fcmToken = doc.data().fcmToken;
                    const payload = {
                        notification: {
                            title: 'New Rating',
                            body: 'A new rating was added by ' + data.user,
                            clickAction: 'FLUTTER_NOTIFICATION_CLICK'
                        },
                        data: {
                            user: data.user,
                            date: data.date,
                            rating: data.rating,
                        }
                    }
                    admin.messaging().sendToDevice(fcmToken, payload).then((response) => {
                        console.log('Successfully sent message:', response);
                    }).catch((error) => {
                        console.log('Error sending message:', error);
                    });

                    // Have to add code to make notification appear in home screen

                }
            }).catch((error) => {
                console.log('Error adding friend notif to database', error);
            });
        }
    }).catch((error) => {
        console.log('Error adding rating to database', error);
    });
});

// function that initially assigns weights to users metrics
exports.assignWeights = functions.https.onRequest((request, response) => {
    const data = request.body;
    const db = admin.firestore();
    const metricsRef = db.collection('metrics');
    const userRef = db.collection('users');
    const user = metricsRef.doc(data.userHandle);
    user.get().then((doc) => {
        if (doc.exists) {
            // user already has metrics, don't overwrite
            console.log('User already has metrics');
            response.send('User already has metrics');
        } else {
            if (userRef.doc(data.uid).type = 'default') {
                // user does not have metrics, assign weights
                for (const metric of data.metrics) {
                    // set the metrics values
                    doc.add(metric).then(() => {;
                        doc.get(metric).set({
                            name: metric.name,
                            public: metric.public,
                            weight: 1/data.metrics.length
                        });
                    }).catch((error) => {
                        console.log('Error adding metrics to database', error);
                        response.send('Error adding metrics to database');
                    });
                }   
            }
            user.set(metrics).then(() => {
                console.log('Successfully added metrics to database');
                response.send('Successfully added metrics to database');
            }).catch((error) => {
                console.log('Error adding metrics to database', error);
                response.send('Error adding metrics to database');
            });
        }
    }).catch((error) => {
        console.log('Error getting metrics', error);
        response.send('Error getting metrics');
    });
});

