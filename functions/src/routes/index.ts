import * as express from "express";
import CheckAuth from "../middleware/CheckAuth";
import { addUserDetails, getUserDetails ,getUserMetrics, updateUserMetrics, addMetricEntry } from "./users";

const router = express.Router();

router.get("/", (req, res) => {
  res.send("works");
});

//user routes
router.put("/user/:userId", CheckAuth, addUserDetails);
router.get("/user/:userId", getUserDetails);
router.get("/user/:userId/metrics", getUserMetrics);
router.put("/user/:userId/metrics", updateUserMetrics);
router.put("/user/:userId/metrics/:metricId/values", addMetricEntry);
router.get("/user", (req, res) => {
  res.send("works");
});

export default router;
