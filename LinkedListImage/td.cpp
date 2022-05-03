#include "imglist.h"

#include <iostream>

using namespace std;
using namespace cs221util;

int main() {
    PNG inimg;
    cout << "Printing inimg: " << endl;
    inimg.readFromFile("input-images/3x4.png");
    ImgList* l = new ImgList(inimg);
    PNG cimg = l->Render(0, 0);
    for (int i = 0; i < inimg.height(); i++) {
        for (int j = 0; j < inimg.width(); i++) {
            HSLAPixel *p = inimg.getPixel(j, i);
            HSLAPixel *c = cimg.getPixel(j, i);

            cout << "x: " << j << ", y: " << i << endl;
            cout << "h: " << p->h << ", copy h: "  << c->h << endl;
            cout << "s: " << p->s << ", copy s: "  << c->s << endl;
            cout << "l: " << p->l << ", copy l: "  << c->l << endl;
            cout << "a: " << p->a << ", copy a: "  << c->a << endl;
        }
    }
    
    delete &cimg;
    delete l;
    return 0;
}