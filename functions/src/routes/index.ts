import * as express from "express";
import CheckAuth from "../middleware/CheckAuth";
import { addUserDetails, getUserDetails } from "./users";

const router = express.Router();

router.get("/", (req, res) => {
  res.send("works");
});

//user routes
router.post("/user", CheckAuth, addUserDetails);
router.get("/user/:userId", getUserDetails);

export default router;