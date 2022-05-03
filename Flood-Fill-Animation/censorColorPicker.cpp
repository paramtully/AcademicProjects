/*
*  File: censorColorPicker.cpp
*  Implements the censor color picker for CPSC 221 PA2.
*
*/

#include "censorColorPicker.h"

#include <math.h> // gives access to sqrt function

/*
*  Useful function for computing the Euclidean distance between two PixelPoints
*/
double PointDistance(PixelPoint a, PixelPoint b) {
  unsigned int diff_x, diff_y;
  if (a.x > b.x)
    diff_x = a.x - b.x;
  else
    diff_x = b.x - a.x;
  if (a.y > b.y)
    diff_y = a.y - b.y;
  else
    diff_y = b.y - a.y;
  return sqrt(diff_x * diff_x + diff_y * diff_y);
}

CensorColorPicker::CensorColorPicker(unsigned int b_width, PixelPoint ctr, unsigned int rad, PNG& inputimage)
{
  // complete your implementation below
  blockwidth = b_width;
  center = ctr;
  radius = rad;
  img = inputimage;
  initBlockyImage();
}

void CensorColorPicker::initBlockyImage() {
  blockyimg.resize(img.width(), img.height());
  for (int y = 0; y < img.height(); y++) {
    for (int x = 0; x < img.width(); x++) {
      if (x % blockwidth == 0 && y % blockwidth == 0)  setFirstPixelBlockAverage(x, y);
      else *blockyimg.getPixel(x, y) = *blockyimg.getPixel(x - (x % blockwidth), y - (y % blockwidth));
    }
  }
}

void CensorColorPicker::setFirstPixelBlockAverage(int x, int y) {
  HSLAPixel *p = blockyimg.getPixel(x, y);
    p->h = 0.0; p->s = 0.0; p->l = 0.0; p->a = 0.0;
    int numPointsInBlock = 0;
    for (int i = 0; i < blockwidth; i++) {
      for (int j = 0; j < blockwidth; j++) {
        if (x + j >= 0 && x + j < img.width() && y + i >= 0 && y + i < img.height()) {
          HSLAPixel *cp = img.getPixel(x + j, y + i);
          p->h += cp->h;
          p->s += cp->s;
          p->l += cp->l;
          p->a += cp->a;
          numPointsInBlock++;
        }
      }
    }
    p->h /= numPointsInBlock; p->s /= numPointsInBlock; p->l /= numPointsInBlock; p->a /= numPointsInBlock;
}

HSLAPixel CensorColorPicker::operator()(PixelPoint p)
{
  // complete your implementation below
  if (PointDistance(p, center) <= radius) return *blockyimg.getPixel(p.x, p.y);
  else return *img.getPixel(p.x, p.y);
}
