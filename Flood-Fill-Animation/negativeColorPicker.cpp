/*
*  File: negativeColorPicker.cpp
*  Implements the negative color picker for CPSC 221 PA2.
*
*/

#include "negativeColorPicker.h"

NegativeColorPicker::NegativeColorPicker(PNG& inputimg)
{
  // complete your implementation below
  img = inputimg;
  
}

HSLAPixel NegativeColorPicker::operator()(PixelPoint p)
{
  // complete your implementation below
  HSLAPixel img_p = *img.getPixel(p.x, p.y);
  img_p.h += 180.0;
  if (img_p.h >= 360.0) img_p.h -= 360.0;
  img_p.l = 1.0 - img_p.l;
  return img_p;
}
