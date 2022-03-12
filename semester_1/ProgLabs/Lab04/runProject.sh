# shellcheck disable=SC2164
cd ../Lab03
javac -d . *.java */*.java */*/*.java
jar cfm Lab03.jar META-INF/MANIFEST.mf Lab03/
cp Lab03.jar ../Lab04/
cd ../Lab04
javac -cp Lab03.jar -d . *.java */*.java */*/*.java
jar cfm Lab04.jar META-INF/MANIFEST.mf Lab04/ Lab03.jar
java -jar Lab04.jar
