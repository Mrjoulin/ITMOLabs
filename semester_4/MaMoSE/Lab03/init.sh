REPO_NAME=mamose_lab_3
REPORT_REPO=report
REPORT_FILENAME=report.xml

git config --global user.name "Matthew"
git config --global user.email matthewthedigital@gmail.com

rm -rf $REPO_NAME
rm -rf ~/.svnrepos/$REPO_NAME

git init $REPO_NAME
cd $REPO_NAME

# SVN report repo init

svnadmin create ~/.svnrepos/$REPO_NAME

svn mkdir -m "Create repository structure." \
    file://$HOME/.svnrepos/$REPO_NAME/trunk \
    file://$HOME/.svnrepos/$REPO_NAME/branches \
    file://$HOME/.svnrepos/$REPO_NAME/tags

svn checkout file://$HOME/.svnrepos/$REPO_NAME/trunk $REPORT_REPO
cd $REPORT_REPO

touch $REPORT_FILENAME
svn add --force $REPORT_FILENAME
svn commit -m "Initial commit" --username="base"
cd ..

# Git repo init

cp -R ../WebLab3/src ./
cp -R ../build/* ./

git add .
git commit -m "Init repository"

echo "Meow" >> src/main/java/com/joulin/lab3/utils/CoordinatesValidation.java
git add .
git commit -m "Meow to file"

echo "Done!"