//
// Created by Param Tully on 2022-02-05.
//

#include "imglist.h"

#include <iostream>

using namespace std;
using namespace cs221util;

int main() {
    HSLAPixel *pixel1 = new HSLAPixel(5, .5, .8);
    PNG *p1 = new PNG(1, 1);
    *(p1->getPixel(0, 0)) = *pixel1;

    ImgList * l1 = new ImgList(*p1);

    cout << "List width: " << l1->GetDimensionX() << endl;
    cout << "List height: " << l1->GetDimensionY() << endl;

    delete pixel1;
    delete p1;

    return 0;
}