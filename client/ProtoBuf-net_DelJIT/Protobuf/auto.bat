mkdir C++
mkdir CS

protoc.exe -I=. --cpp_out=./C++ Message.proto

protogen -i:Message.proto -o:./CS/Message.cs 
