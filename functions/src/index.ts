import * as functions from "firebase-functions";
import * as express from "express";
import * as cors from "cors";
import routes from "./routes/index";

const app = express();
app.use(cors());
app.options("*", cors());

app.use("/", routes);

exports.api = functions.https.onRequest(app);

exports.pubsub = require("./pubsub/index");
