# ece452-group2

Credit to Open Source projects Used in this Project:

- Google: https://github.com/FirebaseExtended/make-it-so-android
- Google: https://github.com/android/codelab-android-compose/blob/main/NavigationCodelab
- Burak: https://itnext.io/navigation-bar-bottom-app-bar-in-jetpack-compose-with-material-3-c57ae317bd00

## Google Service account: Needed to use firebase

- generate new private key: https://console.firebase.google.com/u/0/project/smilie-90a63/settings/serviceaccounts/adminsdk
- add the file as "google-services.json" to /app root

## configure app config

- firebase console -> settings -> service accounts -> download private key
- copy to root of project file and RENAME to appConfig.json
- IMPORTANT: make sure it doesnt get pushed to repo, that is why appConfig.json was added to gitignore

## how to run firebase functions locally

- cd functions
- npm i
- npm run server
- example url in postman: http://127.0.0.1:5001/smilie-90a63/us-central1/api/user/Amf9aiievGSTYdNuj3Wmg49if8C3
- all api routes will be under /api, example /api/user

## download firestore content

- `npx -p node-firestore-import-export firestore-export -a appConfig.json -b backup.json`

## Adding images to downloads folder in emulator for profile image selection

- device file explorer (bottom left) -> storage -> emulated -> 0 -> Download
- right click on Download folder -> click upload -> add images from /ece452-group2/tempContent

