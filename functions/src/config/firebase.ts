import * as admin from "firebase-admin";

const serviceAccount = require("../../../appConfig.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: `https://${serviceAccount.project_id}.firebaseio.com`,
});

//connect to database
const db = admin.firestore();

//connect to storage
const store = admin.storage();

//connect to authentication
const auth = admin.auth();

//connect to messaging
const messanger = admin.messaging();

export { admin, auth, db, store, messanger };
