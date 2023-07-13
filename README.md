# ece452-group2

Credit to Open Source projects Used in this Project:
- Google: https://github.com/FirebaseExtended/make-it-so-android
- Google: https://github.com/android/codelab-android-compose/blob/main/NavigationCodelab
- Burak: https://itnext.io/navigation-bar-bottom-app-bar-in-jetpack-compose-with-material-3-c57ae317bd00

## Export database data to json

- firebase console -> settings -> service accounts ->  download private key
- copy to root of project file and RENAME to appConfig.json
- IMPORTANT: make sure it doesnt get pushed to repo, that is why appConfig.json was added to gitignore
- `npx -p node-firestore-import-export firestore-export -a appConfig.json -b backup.json`