# 🔨 Comment Rebuild l'Application Mobile

## Méthode 1 : Via Android Studio (RECOMMANDÉ)

### Option A : Rebuild complet
1. Dans Android Studio, cliquez sur **Build** (menu du haut)
2. Sélectionnez **Rebuild Project**
3. Attendez que la compilation se termine
4. Lancez l'application

### Option B : Clean + Build
1. **Build** → **Clean Project**
2. Attendez la fin
3. **Build** → **Rebuild Project**
4. Lancez l'application

## Méthode 2 : Via Terminal

```bash
cd /home/nk-wilfried/Documents/projects/blood_link/mobile

# Clean
./gradlew clean

# Build
./gradlew build

# Ou les deux en une commande
./gradlew clean build
```

## 🚀 Procédure complète de test

### 1. Nettoyer le backend
```bash
cd /home/nk-wilfried/Documents/projects/blood_link/backend
./clean_db.sh
```

### 2. Rebuild le mobile
Dans Android Studio : **Build** → **Rebuild Project**

### 3. Ouvrir Logcat
- Cliquez sur l'onglet **Logcat** en bas
- Filtrez par : `tag:SignUpScreen|tag:AuthState|tag:UserRoleDeserializer`

### 4. Lancer l'application
- Cliquez sur le bouton ▶️ (Run)
- Ou appuyez sur **Shift + F10**

### 5. Tester l'inscription
1. Remplissez le formulaire
2. Cliquez sur "Register"
3. **Regardez les logs dans Logcat**

## ⚠️ Important

- **Toujours rebuild** après avoir modifié le code Kotlin
- **Vérifiez Logcat** pour voir les logs en temps réel
- **Partagez les logs** si ça ne fonctionne toujours pas

## 📊 Logs attendus

Si tout fonctionne, vous devriez voir :
```
SignUpScreen: === STARTING REGISTRATION ===
AuthState: === REGISTRATION START ===
UserRoleDeserializer: === DESERIALIZING ROLE ===
UserRoleDeserializer: Successfully converted to: DONOR
AuthState: === REGISTRATION SUCCESS ===
SignUpScreen: === REGISTRATION COMPLETE ===
```

Si ça crash, vous verrez :
```
=== ERROR ===
ou
=== EXCEPTION ===
```

**Copiez tous les logs et partagez-les pour qu'on puisse identifier le problème !**
