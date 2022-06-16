cmake_minimum_required(VERSION 3.4.1)
set (CMAKE_CXX_STANDARD 11)

set(CMAKE_VERBOSE_MAKEFILE ON)

# strip binaries
set(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -s")
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -s")

add_library( native_lib
             SHARED
             bzip/blocksort.c
             bzip/bzip2.c
             bzip/bzip2recover.c
             bzip/bzlib.c
             bzip/compress.c
             bzip/crctable.c
             bzip/decompress.c
             bzip/dlltest.c
             bzip/huffman.c
             bzip/mk251.c
             bzip/randtable.c
             bzip/spewG.c
             bzip/unzcrash.c
             bspatch.c
             PatchUtils.cpp)

target_include_directories(native_lib
                           PRIVATE
                           bzip)
find_library(log_lib
             log)

target_link_libraries(
        native_lib
        android
        ${log_lib}
)

set(distribution_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../../../libs)
set_target_properties(native_lib
        PROPERTIES
        LIBRARY_OUTPUT_DIRECTORY
        "${distribution_DIR}/${ANDROID_ABI}")