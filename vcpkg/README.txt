# Install vcpkg
Run vcpkg install zlib
Run cmake.exe -DCMAKE_TOOLCHAIN_FILE=C:/devtools/vcpkg/scripts/buildsystems/vcpkg.cmake -G "Visual Studio 15 2017 Win64" . 
Run cmake --build .
