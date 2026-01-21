#include "version.h"

char* get_version() {
  #if defined __STDC_VERSION__
    if(__STDC_VERSION__ == 202000)
        return "C23";
    else if (__STDC_VERSION__ == 201710L)
        return "C17";
    else if (__STDC_VERSION__ == 201112L)
        return "C11";
    else if (__STDC_VERSION__ == 199901L)
        return "C99";
  #else
    return "C90";
  #endif
}