// File:        imglist.cpp
// Date:        2022-01-27 10:21
// Description: Contains partial implementation of ImgList class
//              for CPSC 221 2021W2 PA1
//              Function bodies to be completed by yourselves
//
// ADD YOUR FUNCTION IMPLEMENTATIONS IN THIS FILE
//

#include "imglist.h"

#include <math.h> // provides fmin and fabs functions

#include <iostream>
using namespace std;

/**************************
* MISCELLANEOUS FUNCTIONS *
**************************/

/*
* This function is NOT part of the ImgList class,
* but will be useful for one of the ImgList functions.
* Returns the "difference" between two hue values.
* PRE: hue1 is a double between [0,360).
* PRE: hue2 is a double between [0,360).
* 
* The hue difference is the absolute difference between two hues,
* but takes into account differences spanning the 360 value.
* e.g. Two pixels with hues 90 and 110 differ by 20, but
*      two pixels with hues 5 and 355 differ by 10.
*/
double HueDiff(double hue1, double hue2) {
  return fmin(fabs(hue1 - hue2), fabs(hue2 + 360 - hue1));
}

/*********************
* CONSTRUCTORS, ETC. *
*********************/

/*
* Default constructor. Makes an empty list
*/
ImgList::ImgList() {
  // set appropriate values for all member attributes here
  northwest = nullptr; southeast = nullptr;
}

/* Creates a list from image data
* PRE: img has dimensions of at least 1x1
*/
ImgList::ImgList(PNG &img) {
    int x = 0;
    int y = 0;

    northwest = new ImgNode();
    northwest->colour = *img.getPixel(x, y);

    if (img.height() <= 1) createSingleRow(x, northwest, img);
    else createRows(x, y, northwest, img);
}


/*
* Copy constructor.
* Creates this this to become a separate copy of the data in otherlist
*/
ImgList::ImgList(const ImgList& otherlist) {
  // build the linked node structure using otherlist as a template
  Copy(otherlist);
}

/*
* Assignment operator. Enables statements such as list1 = list2;
*   where list1 and list2 are both variables of ImgList type.
* POST: the contents of this list will be a physically separate copy of rhs
*/
ImgList& ImgList::operator=(const ImgList& rhs) {
  // Re-build any existing structure using rhs as a template

  if (this != &rhs) { // if this list and rhs are different lists in memory
    // release all existing heap memory of this list
    Clear();

    // and then rebuild this list using rhs as a template
    Copy(rhs);
  }

  return *this;
}

/*
* Destructor.
* Releases any heap memory associated with this list.
*/
ImgList::~ImgList() {
  // Ensure that any existing heap memory is deallocated
  Clear();
}

/************
* ACCESSORS *
************/

/*
* Returns the horizontal dimension of this list (counted in nodes)
* Note that every row will contain the same number of nodes, whether or not
*   the list has been carved.
* We expect your solution to take linear time in the number of nodes in the
*   x dimension.
*/
unsigned int ImgList::GetDimensionX() const {
  // replace the following line with your implementation
  return countX(northwest);
}

// EFFECTS: finds x dimension of list
unsigned int ImgList::countX(ImgNode *node) const {
  if (node == nullptr) return 0;
  else return 1 + countX(node->east);
}

/*
* Returns the vertical dimension of the list (counted in nodes)
* It is useful to know/assume that the grid will never have nodes removed
*   from the first or last columns. The returned value will thus correspond
*   to the height of the PNG image from which this list was constructed.
* We expect your solution to take linear time in the number of nodes in the
*   y dimension.
*/
unsigned int ImgList::GetDimensionY() const {
  // replace the following line with your implementation
  return countY(northwest);
}

// EFFECTS: finds y dimension of list
unsigned int ImgList::countY(ImgNode *node) const {
  if (node == nullptr) return 0;
  else return 1 + countY(node->south);
}

/*
* Returns the horizontal dimension of the list (counted in original pixels, pre-carving)
* The returned value will thus correspond to the width of the PNG image from
*   which this list was constructed.
* We expect your solution to take linear time in the number of nodes in the
*   x dimension.
*/
unsigned int ImgList::GetDimensionFullX() const {
  // replace the following line with your implementation
  return countFullX(northwest);
}

// EFFECTS: finds x dimension of list
unsigned int ImgList::countFullX(ImgNode *node) const {
  if (node == nullptr) return 0;
  else return 1 + node->skipright + countFullX(node->east);
}

/*
* Returns a pointer to the node which best satisfies the specified selection criteria.
* The first and last nodes in the row cannot be returned.
* PRE: rowstart points to a row with at least 3 physical nodes
* PRE: selectionmode is an integer in the range [0,1]
* PARAM: rowstart - pointer to the first node in a row
* PARAM: selectionmode - criterion used for choosing the node to return
*          0: minimum luminance across row, not including extreme left or right nodes
*          1: node with minimum total of "hue difference" with its left neighbour and with its right neighbour.
*        In the (likely) case of multiple candidates that best match the criterion,
*        the left-most node satisfying the criterion (excluding the row's starting node)
*        will be returned.
* A note about "hue difference": For PA1, consider the hue value to be a double in the range [0, 360).
* That is, a hue value of exactly 360 should be converted to 0.
* The hue difference is the absolute difference between two hues,
* but be careful about differences spanning the 360 value.
* e.g. Two pixels with hues 90 and 110 differ by 20, but
*      two pixels with hues 5 and 355 differ by 10.
*/
ImgNode *ImgList::SelectNode(ImgNode *rowstart, int selectionmode) {
    // add your implementation below
    // TODO: Done
    ImgNode *curr = rowstart->east;
    ImgNode *min = curr;
    int minHueDiff = INT_MAX;

    while (curr->east) {
        if (selectionmode == 0 && curr->colour.l < min->colour.l) min = curr;
        else if (selectionmode == 1) {
            int cHueDiff = HueDiff(curr->colour.h, curr->west->colour.h) +
                           HueDiff(curr->colour.h, curr->east->colour.h);
            if (cHueDiff < minHueDiff) {
                min = curr;
                minHueDiff = cHueDiff;
            }
        }
        curr = curr->east;
    }
    return min;
}

/*
* Renders this list's pixel data to a PNG, with or without filling gaps caused by carving.
* PRE: fillmode is an integer in the range of [0,2]
* PARAM: fillgaps - whether or not to fill gaps caused by carving
*          false: render one pixel per node, ignores fillmode
*          true: render the full width of the original image,
*                filling in missing nodes using fillmode
* PARAM: fillmode - specifies how to fill gaps
*          0: solid, uses the same colour as the node at the left of the gap
*          1: solid, using the averaged values (all channels) of the nodes at the left and right of the gap
*             Note that "average" for hue will use the closer of the angular distances,
*             e.g. average of 10 and 350 will be 0, instead of 180.
*             Average of diametric hue values will use the smaller of the two averages
*             e.g. average of 30 and 210 will be 120, and not 300
*                  average of 170 and 350 will be 80, and not 260
*          2: *** OPTIONAL - FOR BONUS ***
*             linear gradient between the colour (all channels) of the nodes at the left and right of the gap
*             e.g. a gap of width 1 will be coloured with 1/2 of the difference between the left and right nodes
*             a gap of width 2 will be coloured with 1/3 and 2/3 of the difference
*             a gap of width 3 will be coloured with 1/4, 2/4, 3/4 of the difference, etc.
*             Like fillmode 1, use the smaller difference interval for hue,
*             and the smaller-valued average for diametric hues
*/
PNG ImgList::Render(bool fillgaps, int fillmode) const {
  // Add/complete your implementation below
  // TODO:
  //PNG outpng; //this will be returned later. Might be a good idea to resize it at some point.

  int width;
  if (fillgaps) width = GetDimensionFullX();
  else width = GetDimensionX();

  // PNG *outpng = new PNG(width, GetDimensionY());          // CORRECTION: dont use new
  PNG outpng(width, GetDimensionY());

  ImgNode* curr = northwest;
  int y = 0;
  while(curr) {
      ImgNode *rowNode = curr;
      int x = 0;
      while (x < width) {
          x = setPixel(x, y, rowNode, outpng, fillgaps, fillmode);
          rowNode = rowNode->east;
      }
      curr = curr->south;
      y++;
  }
  return outpng;
}

int ImgList::setPixel(int x, int y, ImgNode *&node, PNG &png, bool fillgaps, int fillmode) const {
    HSLAPixel *pixel = png.getPixel(x, y);
    *pixel = node->colour;
    x++;

    if (fillgaps && (fillmode == 0 || fillmode == 1) && node->skipright) {
      int max = x + node->skipright;
        while (x < max) {
            HSLAPixel *fillPixel = png.getPixel(x, y);
            if (fillmode == 0) *fillPixel = *pixel;
            else if (fillmode == 1) {
              double ad1 = (node->colour.h + node->east->colour.h) / 2;
              double ad2 = ad1 + 180;
              if (ad2 >= 360) ad2 -= 360;
              if (abs(node->colour.h - ad1) < abs(node->colour.h - ad2)) {
                fillPixel->h =  ad1;
              } else if (abs(node->colour.h - ad1) > abs(node->colour.h - ad2)) {
                fillPixel->h =  ad2;
              } else {
                fillPixel->h =  min (ad1 / 2, ad2 / 2);
              }
              fillPixel->l = (node->colour.l + node->east->colour.l) / 2;
              fillPixel->a = (node->colour.a + node->east->colour.a) / 2;
              fillPixel->s = (node->colour.s + node->east->colour.s) / 2;
            }
            x++;
        }
    } else if (fillgaps && fillmode == 2 && node->skipright) {
        int numFill = node->skipright;
        double gradient;
        double ad1 = (node->colour.h + node->east->colour.h) / 2;
        double ad2 = ad1 + 180;
        if (ad2 >= 360) ad2 -= 360;
        if (abs(node->colour.h - ad1) < abs(node->colour.h - ad2)) {
            gradient =  ad1 / numFill;
        } else if (abs(node->colour.h - ad1) > abs(node->colour.h - ad2)) {
           gradient =  ad2 / numFill;
        } else {
            gradient =  min(ad1 / 2, ad2 / 2) / numFill;
        }
        int max = x + numFill;
        int i = 1;
        while (x < max) {
            HSLAPixel *fillPixel = png.getPixel(x, y);
            fillPixel->h = gradient * i;
            fillPixel->l = (node->colour.l + node->east->colour.l) / numFill * i;
            fillPixel->a = (node->colour.a + node->east->colour.a) / 2;
            fillPixel->s = (node->colour.s + node->east->colour.s) / (2*numFill) * i;
            x++;
            i++;
        }
    }
    return x;
}

/************
* MODIFIERS *
************/

/*
* Removes exactly one node from each row in this list, according to specified criteria.
* The first and last nodes in any row cannot be carved.
* PRE: this list has at least 3 nodes in each row
* PRE: selectionmode is an integer in the range [0,1]
* PARAM: selectionmode - see the documentation for the SelectNode function.
* POST: this list has had one node removed from each row. Neighbours of the created
*       gaps are linked appropriately, and their skip values are updated to reflect
*       the size of the gap.
*/
void ImgList::Carve(int selectionmode) {
  // TODO : add your implementation here
  ImgNode *curr = northwest;
  for (int i = 0; i < GetDimensionY(); i++) {
      ImgNode *toRemove = SelectNode(northwest, selectionmode);

      detatchHorizontally(toRemove);
      if (toRemove->north) detatchNorth(toRemove);
      if (toRemove->south) detatchSouth(toRemove);
  }
}

void ImgList::detatchHorizontally(ImgNode *&toRemove) {
    toRemove->east->west = toRemove->west;
    toRemove->west->east = toRemove->east;
    toRemove->east->skipleft += toRemove->skipleft + 1;
    toRemove->west->skipright += toRemove->skipright + 1;

}

void ImgList::detatchNorth(ImgNode *&toRemove) {
    toRemove->north->south = toRemove->south;
    toRemove->north->skipdown = toRemove->skipdown + 1;
}

void ImgList::detatchSouth(ImgNode *&toRemove) {
    toRemove->south->north = toRemove->north;
    toRemove->south->skipdown = toRemove->skipup + 1;
}

// note that a node on the boundary will never be selected for removal
/*
* Removes "rounds" number of nodes (up to a maximum of node width - 2) from each row,
* based on specific selection criteria.
* Note that this should remove one node from every row, repeated "rounds" times,
* and NOT remove "rounds" nodes from one row before processing the next row.
* PRE: selectionmode is an integer in the range [0,1]
* PARAM: rounds - number of nodes to remove from each row
*        If rounds exceeds node width - 2, then remove only node width - 2 nodes from each row.
*        i.e. Ensure that the final list has at least two nodes in each row.
* POST: this list has had "rounds" nodes removed from each row. Neighbours of the created
*       gaps are linked appropriately, and their skip values are updated to reflect
*       the size of the gap.
*/
void ImgList::Carve(unsigned int rounds, int selectionmode) {
  // TODO: add your implementation here
  int range = min(rounds, GetDimensionFullX() - 2);
  ImgNode *node = northwest;
  for (int i = 0; i < GetDimensionY(); i++) {
      for (int j = 0; j < range; j++) {
          ImgNode *toRemove = SelectNode(node, selectionmode);
          detatchHorizontally(toRemove);
          if (toRemove->north) detatchNorth(toRemove);
          if (toRemove->south) detatchSouth(toRemove);
      }
      node = node->south;
  }
}


/*
* Helper function deallocates all heap memory associated with this list,
* puts this list into an "empty" state. Don't forget to set your member attributes!
* POST: this list has no currently allocated nor leaking heap memory,
*       member attributes have values consistent with an empty list.
*/
void ImgList::Clear() {
  // add your implementation here
  // TODO: Done
    if (!northwest) return;
    else clearColumn(northwest);

}

void ImgList::clearColumn(ImgNode *node) {
    if(!node) return;
    else {
        clearColumn(node->south);
        clearRow(node);
    }
}

void ImgList::clearRow(ImgNode *node) {
    if (!node) return;
    else {
        clearRow(node->east);
        delete node;
    }
}

/* ************************
*  * OPTIONAL - FOR BONUS *
** ************************
* Helper function copies the contents of otherlist and sets this list's attributes appropriately
* PRE: this list is empty
* PARAM: otherlist - list whose contents will be copied
* POST: this list has contents copied from by physically separate from otherlist
*/
void ImgList::Copy(const ImgList& otherlist) {
  // add your implementation here
  // TODO:
}

/*************************************************************************************************
* IF YOU DEFINED YOUR OWN PRIVATE FUNCTIONS IN imglist.h, YOU MAY ADD YOUR IMPLEMENTATIONS BELOW *
*************************************************************************************************/

