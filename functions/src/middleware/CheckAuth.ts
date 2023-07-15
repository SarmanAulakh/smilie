import {auth} from "../config/firebase"
import { Request, Response, NextFunction } from "express"
 
export default function(req: Request, res: Response, next:NextFunction){
  let idToken = "";
  const authHead = req.headers.authorization;

  if(authHead && authHead.startsWith('Bearer ')){
    idToken = authHead.split('Bearer ')[1]
  }else{
    console.log("No token found")
    res.status(403).json({ error: "Unauthorized" })
  }

  try {
    auth.verifyIdToken(idToken)
    next()
  } catch (error) {
    console.log("Error while verifying token: ", error)
    res.status(403).json(error)
  }
}
