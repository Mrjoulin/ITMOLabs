#!/bin/bash

mkdir -p lab0 && cd lab0

# Step 1

mkdir -p duskull3/gyarados duskull3/porygon2
mkdir -p pansear1
mkdir -p volcarona5/skiploom volcarona5/bulbasaur \
         volcarona5/magnezone volcarona5/jigglypuff

echo -e "Возможности  Overland=3 Surface=1 Sky=6 Jump=3 Power1=0\nIntelligence=4 Guster=0" > duskull3/vullaby
echo -e "satk=4 sdef=7\nspd=5" > duskull3/scraggy
echo -e "weigth=32.4 height=35.0 atk=8 def=7" > herdier3
echo -e "Развитые\nспособности Sap Sipper" > leafeon5
echo -e "Тип диеты  Herbivore" > pansear1/ampharos
echo -e "Тип\nдиеты  Terravore" > pansear1/trubbish
echo -e "weigth=20.9 height=24.0 atk=11\ndef=5" > pansear1/archen
echo -e "Живет  Forest Grassland Rainforest" > roserade0
echo -e "Живет\nOcean" > volcarona5/wailmer

# Step 2

chmod 335 duskull3
chmod 573 duskull3/gyarados
chmod 736 duskull3/porygon2
chmod 006 duskull3/vullaby duskull3/scraggy herdier3
chmod 404 leafeon5
chmod 315 pansear1
chmod a-wx pansear1/ampharos
chmod 624 pansear1/trubbish
chmod 044 pansear1/archen
chmod 440 roserade0
chmod 500 volcarona5
chmod 440 volcarona5/wailmer
chmod 550 volcarona5/skiploom
chmod 751 volcarona5/bulbasaur
chmod 330 volcarona5/magnezone
chmod 737 volcarona5/jigglypuff

# Step 3

# Step 3.1
chmod u+r pansear1 pansear1/archen && chmod u+w duskull3/gyarados &&
      cp -r ./pansear1 ./duskull3/gyarados &&
      chmod u-r pansear1 pansear1/archen && chmod u-w duskull3/gyarados
# Step 3.2
cat roserade0 > pansear1/archenroserade
# Step 3.3
ln leafeon5 pansear1/ampharosleafeon
# Step 3.4
chmod u+w volcarona5 && cp ./roserade0 ./volcarona5/bulbasaur && chmod u-w volcarona5
# Step 3.5
ln -s ./pansear1 ./Copy_19
# Step 3.6
chmod u+r pansear1 && cat ./pansear1/trubbish ./pansear1/trubbish > ./roserade0_39 && chmod u-r pansear1
# Step 3.7
ln -s ../leafeon5 pansear1/archenleafeon

# Show current file and directory hierarchy

echo "Current file and directory hierarchy:"

ls -lR

# Step 4
echo -e "\nStep 4.1:"
wc -l *3 */*3 */*/*3 2> /dev/null | sort -n | grep -vw "total"

echo "Step 4.2:"
ls -p volcarona5 2> /tmp/someerror | grep -v /

echo "Step 4.3:"
cd volcarona5 && cat -n $(ls -p | grep -v /) 2>&1 | sort -r && cd ..

echo "Step 4.4:"
ls -l pansear1 2> /dev/null | sort -d

echo "Step 4.5:"
ls -l volcarona5/* volcarona5/*/* 2> /tmp/othererror | sort -d | grep "^-.*/.*"

echo "Step 4.6:"
cat -n herdier3 2> /dev/null | egrep -v ".*(e|E)$"

# Step 5

rm -rf leafeon5
rm -rf pansear1/ampharos
rm -rf pansear1/archenleafe*
rm -rf pansear1/ampharosleafe*
rm -rf pansear1
rmdir volcarona5/skiploom
