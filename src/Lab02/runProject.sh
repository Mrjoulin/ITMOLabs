javac -cp Pokemon.jar -d . *.java */*.java */*/*.java
cp utils/*.csv Lab02/utils/
jar cfm Lab02.jar META-INF/MANIFEST.mf Lab02/ Pokemon.jar
java -jar Lab02.jar
