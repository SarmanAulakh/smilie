import * as express from "express";
import CheckAuth from "../middleware/CheckAuth";
import { addUserDetails, getUserDetails } from "./users";

const router = express.Router();

router.get("/", (req, res) => {
  res.send("works");
});

//user routes
router.put("/user/:userId", CheckAuth, addUserDetails);
router.get("/user/:userId", getUserDetails);
router.get("/user", (req, res) => {
  res.send("works");
});

export default router;
