import * as express from "express";
// import CheckAuth from "../middleware/CheckAuth";
import {
  addUserDetails,
  getUserDetails,
  getUserMetrics,
  updateUserMetrics,
  addMetricEntry,
  createNewUser,
  getAllUsers,
} from "./users";

const router = express.Router();

router.get("/", (req, res) => {
  res.send("works");
});

//user routes
router.post("/users", createNewUser);
router.get("/users", getAllUsers);
router.put("/users/:userId", addUserDetails);
router.get("/users/:userId", getUserDetails);
router.get("/users/:userId/metrics", getUserMetrics);
router.put("/users/:userId/metrics", updateUserMetrics);
router.put("/users/:userId/metrics/:metricId", addMetricEntry);

export default router;
