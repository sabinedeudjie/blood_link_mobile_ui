# Blood Link Mobile UI

Ce README récapitule ce qui a été mis en place pour l’authentification et comment lancer l’appli avec ton serveur.

## Ce qui a été fait
- **Base URL backend**: `http://192.168.1.117:8080/api/` (dans `app/src/main/java/com/example/bloodlink/retrofit/RetrofitInstance.kt`).
- **Routes corrigées**: toutes les APIs Retrofit utilisent maintenant les chemins `/api/v1/...` (ex: `v1/auth`, `v1/user`, etc.) et l’intercepteur JWT ignore les endpoints d’auth.
- **Logs réseau**: Logging interceptor en niveau `BODY` pour faciliter le debug des requêtes/réponses.
- **HTTP clair autorisé**: ajout du domaine local dans `app/src/main/res/xml/network_security_config.xml` (`192.168.1.117`).
- **Feedback login**: l’écran de connexion affiche les erreurs backend et réinitialise l’état de chargement.

## Comment ça marche (auth)
- **Appel login**: `AuthState.login(email, password, context)` → `AuthenticationApi.authenticate()` → enregistre le JWT via `TokenManager` → met à jour le rôle/utilisateur courant → navigation vers le dashboard selon le rôle.
- **Appel signup**: `AuthState.registerUser(request, context)` → `AuthenticationApi.signUp()` → stocke le JWT → récupère le profil courant via `UserApi.getMe()` → navigation vers le dashboard selon le rôle choisi.
- **Token**: stocké via Jetpack DataStore (`TokenManager.tokenFlow`) et injecté dans les requêtes par `JWTInterceptor` (sauf pour `logIn`, `signUp`, `logout`).
- **Base Retrofit**: `RetrofitInstance.getRetrofit(context)` fournit un Retrofit configuré avec le client OkHttp + intercepteur JWT + logs réseau.

## Prérequis côté backend
- Backend accessible depuis l’appareil (ou l’émulateur) à `http://192.168.1.117:8080/api`.
- Endpoints attendus:
  - Auth: `POST /api/v1/auth/logIn`, `POST /api/v1/auth/signUp`, `GET /api/v1/auth/current-user`
  - User: `POST /api/v1/user/getMe`
  - Les autres services suivent le même préfixe `/api/v1/...`

## Lancer et tester
1) Assure-toi que le backend tourne et est joignable depuis l’appareil/émulateur (ping ou curl l’URL de base).
2) Ouvre le projet dans Android Studio.
3) Construis/lance sur un device connecté au même réseau que `192.168.1.117`.
4) Teste **inscription** puis **connexion** ; surveille le logcat (tags: `AuthState`, `LoginScreen`, `SignUp`).

## Dépannage
- **Gradle lock**: si le build échoue avec `journal-1.lock`, arrête le PID indiqué (ex: `ps -p <pid> -o pid,cmd` puis `kill <pid>`), puis relance le build.
- **Erreur réseau**: vérifie la connectivité vers `192.168.1.117:8080` et que le backend répond sur `/api/v1/...`. Ajuste `BACKEND_URL` si l’IP change.
- **HTTP non autorisé**: l’IP locale doit rester dans `network_security_config.xml` pour les requêtes en clair.

## Fichiers clés
- `app/src/main/java/com/example/bloodlink/retrofit/RetrofitInstance.kt`: config Retrofit/OkHttp, base URL.
- `app/src/main/java/com/example/bloodlink/retrofit/JWTInterceptor.kt`: injection du header `Authorization`.
- `app/src/main/java/com/example/bloodlink/retrofit/AuthenticationApi.kt`: endpoints auth.
- `app/src/main/java/com/example/bloodlink/data/AuthState.kt`: logique login/signup + stockage token.
- `app/src/main/java/com/example/bloodlink/ui/screens/auth/LoginScreen.kt`: UI login avec affichage d’erreur.
