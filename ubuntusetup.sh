echo "Updating and upgrading packages"
sudo apt update -y
sudo apt upgrade -y
echo "Installing required softwares"
sudo apt install git maven openjdk-8-jdk -y
sudo apt install build-essential -y
sudo apt install mysql-client -y
echo "Installing build dependencies"
sudo apt install autoconf cmake g++ -y

echo "Preparing to get protoc and libndbclient"
mkdir deps
cd deps

echo "Installing protoc 3.2"
# Make sure you grab the latest version
protocv=3.2.0
protoczip=protoc-$protocv-linux-x86_64.zip

curl -OL https://github.com/google/protobuf/releases/download/v$protocv/$protoczip

# Unzip
unzip $protoczip -d protoc3
rm $protoczip

# Move only protoc* to /usr/bin/
sudo ln -s $(pwd)/protoc3/bin/protoc /usr/bin/protoc

echo "Installing libndbclient.so"
cd /tmp
wget http://kompics.sics.se/maven/repository/com/mysql/ndb/clusterj-native/7.5.4/clusterj-native-7.5.4-natives-linux.jar
unzip clusterj-native-7.5.4-natives-linux.jar
sudo cp libndbclient.so /usr/lib
cd



