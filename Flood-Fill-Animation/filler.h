/*
*  File:        filler.h
*  Description: Defintion of a filler namespace.
*
*/
#ifndef _FILLER_H_
#define _FILLER_H_

#include <iostream>

#include "cs221util/PNG.h"

#include "animation.h"
#include "pixelpoint.h"
#include "priority.h"
#include "colorPicker.h"
#include "queue.h"
#include "stack.h"
#include <vector>
#include <utility>
#include <iostream>

using namespace std;
using namespace cs221util;

/*
*  filler namespace: specifies a set of functions for performing flood
*  fills on images.
*
*/
namespace filler
{

  /*
  * Struct to store configuration variables for filler function.
  */
  struct FillerConfig
  {
    int frameFreq;                      // Frequency with which to save frames to GIF animation.
    double tolerance;                    // Tolerance used to determine if pixel is in fill region.
    PNG img;                            // Image to perform the fill on.
    PixelPoint seedpoint;               // Seed location where the fill will begin.
    PriorityNeighbours neighbourorder;  // Structure for determining order of neighbours to explore (to add to the ordering structure)
    ColorPicker* picker;                // colorPicker used to fill the region.
  };

  /*
  *  Performs a flood fill using breadth first search.
  *
  *  PARAM:  config - FillerConfig struct to setup the fill.
  *  RETURN: animation object illustrating progression of flood fill algorithm.
  */
  animation FillBFS(FillerConfig& config);

  /*
  *  Performs a flood fill using depth first search.
  *
  *  PARAM:  config - FillerConfig struct to setup the fill.
  *  RETURN: animation object illustrating progression of flood fill algorithm.
  */
  animation FillDFS(FillerConfig& config);

  /*
  *  Run a flood fill on an image starting at the seed point.
  *
  *  PARAM: config - FillerConfig struct with data for flood fill of image.
  *  RETURN: animation object illustrating progression of flood fill algorithm.
  */
  template <template <class T> class OrderingStructure> animation Fill(FillerConfig& config);

  // Add any helper functions here

  /*
  *  Adds point to OS and marks point by changing color
  *
  *  PARAM: config - OrderingStructure to take in point, and PixelPoint to process and
  *                  insert into struct
  */
  // void addPointToOS(OrderingStructure<PixelPoint> &os, PixelPoint &point, vector<vector<bool>> &pixelMap);
  // void setPixelPointMarked(PixelPoint &point, vector<vector<bool>> &pixelMap);
  // void addNeighboursToOrder(OrderingStructure<PixelPoint> &os, PixelPoint &point, 
  //      FillerConfig &config, vector<vector<bool>> &pixelMap);
  // bool isValidNeighbour(int x, int y, PixelPoint &point, FillerConfig &config, vector<vector<bool>> &pixelMap);


  ////////////////////////
  bool isWithinImage(int x, int y, FillerConfig &config);
  bool isWithinTolerance(int x, int y, FillerConfig &config);
  void addNeighbours(int x, int y, vector<vector<PixelPoint>> &map, OrderingStructure<PixelPoint>& os, FillerConfig &config);
  void initMap(vector<vector<PixelPoint>> &map, FillerConfig &config);
  bool isVisited(int x, int y, vector<vector<PixelPoint>> &map);

} // namespace filler

#include "filler.cpp"

#endif
