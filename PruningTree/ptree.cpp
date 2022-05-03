/*
*  File:        ptree.cpp
*  Description: Implementation of a partitioning tree class for CPSC 221 PA3
*  Date:        2022-03-03 01:53
*
*               ADD YOUR PRIVATE FUNCTION IMPLEMENTATIONS TO THE BOTTOM OF THIS FILE
*/

#include "ptree.h"
#include "hue_utils.h" // useful functions for calculating hue averages
#include "ptree-private.h"

using namespace cs221util;
using namespace std;

// The following definition may be convenient, but is not necessary to use
typedef pair<unsigned int, unsigned int> pairUI;

/////////////////////////////////
// PTree private member functions
/////////////////////////////////

/*
*  Destroys all dynamically allocated memory associated with the current PTree object.
*  You may want to add a recursive helper function for this!
*  POST: all nodes allocated into the heap have been released.
*/
void PTree::Clear() {
  // add your implementation below
  clear(root);
}

/*
*  Copies the parameter other PTree into the current PTree.
*  Does not free any memory. Should be called by copy constructor and operator=.
*  You may want a recursive helper function for this!
*  PARAM: other - the PTree which will be copied
*  PRE:   There is no dynamic memory associated with this PTree.
*  POST:  This PTree is a physically separate copy of the other PTree.
*/
void PTree::Copy(const PTree& other) {
  // add your implementation below
  copy(other.root, root);
}

/*
*  Private helper function for the constructor. Recursively builds the tree
*  according to the specification of the constructor.
*  You *may* change this if you like, but we have provided here what we
*  believe will be sufficient to use as-is.
*  PARAM:  im - full reference image used for construction
*  PARAM:  ul - upper-left image coordinate of the currently building Node's image region
*  PARAM:  w - width of the currently building Node's image region
*  PARAM:  h - height of the currently building Node's image region
*  RETURN: pointer to the fully constructed Node
*/
Node* PTree::BuildNode(PNG& im, pair<unsigned int, unsigned int> ul, unsigned int w, unsigned int h) {
  // replace the line below with your implementation
  if (w == 0 || h == 0) return nullptr;
  Node *cNode = new Node(ul, w, h, buildNodePixel(im, ul, w, h));
  if (h > w) {
    cNode->A = BuildNode(im, ul, w, h/2);
    cNode->B = BuildNode(im, pair<unsigned int, unsigned int>(ul.first, ul.second + h/2), 
                         w, (h == 1? h/2 : ciel(h/2.0)) );
  } else {
    cNode->A = BuildNode(im, ul, w/2, h);
    cNode->B = BuildNode(im, pair<unsigned int, unsigned int>(ul.first + w/2, ul.second), 
                         (w == 1? w/2 : ciel(w/2.0)) , h);
  }
  return cNode;
}

////////////////////////////////
// PTree public member functions
////////////////////////////////

/*
*  Constructor that builds the PTree using the provided PNG.
*
*  The PTree represents the sub-image (actually the entire image) from (0,0) to (w-1, h-1) where
*  w-1 and h-1 are the largest valid image coordinates of the original PNG.
*  Each node corresponds to a rectangle of pixels in the original PNG, represented by
*  an (x,y) pair for the upper-left corner of the rectangle, and two unsigned integers for the
*  number of pixels on the width and height dimensions of the rectangular sub-image region the
*  node defines.
*
*  A node's two children correspond to a partition of the node's rectangular region into two
*  equal (or approximately equal) size regions which are either tiled horizontally or vertically.
*
*  If the rectangular region of a node is taller than it is wide, then its two children will divide
*  the region into vertically-tiled sub-regions of equal height:
*  +-------+
*  |   A   |
*  |       |
*  +-------+
*  |   B   |
*  |       |
*  +-------+
*
*  If the rectangular region of a node is wider than it is tall, OR if the region is exactly square,
*  then its two children will divide the region into horizontally-tiled sub-regions of equal width:
*  +-------+-------+
*  |   A   |   B   |
*  |       |       |
*  +-------+-------+
*
*  If any region cannot be divided exactly evenly (e.g. a horizontal division of odd width), then
*  child B will receive the larger half of the two subregions.
*
*  When the tree is fully constructed, each leaf corresponds to a single pixel in the PNG image.
* 
*  For the average colour, this MUST be computed separately over the node's rectangular region.
*  Do NOT simply compute this as a weighted average of the children's averages.
*  The functions defined in hue_utils.h and implemented in hue_utils.cpp will be very useful.
*  Computing the average over many overlapping rectangular regions sounds like it will be
*  inefficient, but as an exercise in theory, think about the asymptotic upper bound on the
*  number of times any given pixel is included in an average calculation.
* 
*  PARAM: im - reference image which will provide pixel data for the constructed tree's leaves
*  POST:  The newly constructed tree contains the PNG's pixel data in each leaf node.
*/
PTree::PTree(PNG& im) {
  // add your implementation below
  root = BuildNode(im, pair<unsigned int, unsigned int>(0.0, 0.0), im.width(), im.height());
}

/*
*  Copy constructor
*  Builds a new tree as a copy of another tree.
*
*  PARAM: other - an existing PTree to be copied
*  POST:  This tree is constructed as a physically separate copy of other tree.
*/
PTree::PTree(const PTree& other) {
  // add your implementation below
  Copy(other);
}

/*
*  Assignment operator
*  Rebuilds this tree as a copy of another tree.
*
*  PARAM: other - an existing PTree to be copied
*  POST:  If other is a physically different tree in memory, all pre-existing dynamic
*           memory in this tree is deallocated and this tree is reconstructed as a
*           physically separate copy of other tree.
*         Otherwise, there is no change to this tree.
*/
PTree& PTree::operator=(const PTree& other) {
  // add your implementation below
  if (this != &other) {
    clear(root);
    root = NULL;
    copy(other.root, root);
  }
  return *this;
}

/*
*  Destructor
*  Deallocates all dynamic memory associated with the tree and destroys this PTree object.
*/
PTree::~PTree() {
  // add your implementation below
  clear(root);
}

/*
*  Traverses the tree and puts the leaf nodes' color data into the nodes'
*  defined image regions on the output PNG.
*  For non-pruned trees, each leaf node corresponds to a single pixel that will be coloured.
*  For pruned trees, each leaf node may cover a larger rectangular region that will be
*  entirely coloured using the node's average colour attribute.
*
*  You may want to add a recursive helper function for this!
*
*  RETURN: A PNG image of appropriate dimensions and coloured using the tree's leaf node colour data
*/
PNG PTree::Render() const {
  // replace the line below with your implementation
  PNG img = PNG(root->width, root->height);
  render(img, root);
  return img;
}

/*
*  Trims subtrees as high as possible in the tree. A subtree is pruned
*  (its children are cleared/deallocated) if ALL of its leaves have colour
*  within tolerance of the subtree root's average colour.
*  Pruning criteria should be evaluated on the original tree, and never on a pruned
*  tree (i.e. we expect that Prune would be called on any tree at most once).
*  When processing a subtree, you should determine if the subtree should be pruned,
*  and prune it if possible before determining if it has subtrees that can be pruned.
* 
*  You may want to add (a) recursive helper function(s) for this!
*
*  PRE:  This tree has not been previously pruned (and is not copied/assigned from a tree that has been pruned)
*  POST: Any subtrees (as close to the root as possible) whose leaves all have colour
*        within tolerance from the subtree's root colour will have their children deallocated;
*        Each pruned subtree's root becomes a leaf node.
*/
void PTree::Prune(double tolerance) {
  // add your implementation below
  prune(root, tolerance);
}

/*
*  Returns the total number of nodes in the tree.
*  This function should run in time linearly proportional to the size of the tree.
*
*  You may want to add a recursive helper function for this!
*/
int PTree::Size() const {
  // replace the line below with your implementation
  return size(root);
}

/*
*  Returns the total number of leaf nodes in the tree.
*  This function should run in time linearly proportional to the size of the tree.
*
*  You may want to add a recursive helper function for this!
*/
int PTree::NumLeaves() const {
  // replace the line below with your implementation
  return numLeaves(root);
}

/*
*  Rearranges the nodes in the tree, such that a rendered PNG will be flipped horizontally
*  (i.e. mirrored over a vertical axis).
*  This can be achieved by manipulation of the nodes' member attribute(s).
*  Note that this may possibly be executed on a pruned tree.
*  This function should run in time linearly proportional to the size of the tree.
*
*  You may want to add a recursive helper function for this!
*
*  POST: Tree has been modified so that a rendered PNG will be flipped horizontally.
*/
void PTree::FlipHorizontal() {
  // add your implementation below
  // flipHorizontal(root);
  flipHorizontal(root, root->width);
}

/*
*  Like the function above, rearranges the nodes in the tree, such that a rendered PNG
*  will be flipped vertically (i.e. mirrored over a horizontal axis).
*  This can be achieved by manipulation of the nodes' member attribute(s).
*  Note that this may possibly be executed on a pruned tree.
*  This function should run in time linearly proportional to the size of the tree.
*
*  You may want to add a recursive helper function for this!
*
*  POST: Tree has been modified so that a rendered PNG will be flipped vertically.
*/
void PTree::FlipVertical() {
  // add your implementation below
  flipVertical(root, root->height);
}

/*
    *  Provides access to the root of the tree.
    *  Dangerous in practice! This is only used for testing.
    */
Node* PTree::GetRoot() {
  return root;
}

//////////////////////////////////////////////
// PERSONALLY DEFINED PRIVATE MEMBER FUNCTIONS
//////////////////////////////////////////////

// 
int PTree::numLeaves(Node *node) const {
  if (!node) return 0;
  else if (isLeaf(node)) return 1;
  else return numLeaves(node->A) + numLeaves(node->B);
}

int PTree::size(Node *node) const {
  if (!node) return 0;
  return 1 + size(node->A) + size(node->B);
}

void PTree::copy(Node *ori, Node *&cpy) {
  if (!ori) return;
  cpy = new Node(*ori);
  copy(ori->A, cpy->A);
  copy(ori->B, cpy->B);
}

HSLAPixel PTree::buildNodePixel(PNG& im, pair<unsigned int, unsigned int> ul, unsigned int width, unsigned int height) {
  pair<double, double> h(0.0, 0.0);
  double s = 0.0, l = 0.0;
  for (unsigned int j = ul.second; j < ul.second + height; j++) {
    for (unsigned int i = ul.first; i < ul.first + width; i++) {
      HSLAPixel *p = im.getPixel(i, j);
      h.first += Deg2X(p->h); h.second += Deg2Y(p->h);
      s += p->s; l += p->l;
    }
  }
  int divisor = width * height;
  h.first /= divisor;  h.second /= divisor;
  s /= divisor; l /= divisor;
  return HSLAPixel(XY2Deg(h.first, h.second), s, l);
}

int PTree::ciel(double d) {
  return (d - (int) d) > 0.0000001 ? (int) d + 1 : (int) d;
}

void PTree::render(PNG &img, Node* node) const {
  if(!node) return;
  if (isLeaf(node)) {
    renderLeaf(img, node);
  }
  else {
    render(img, node->A);
    render(img, node->B);
  }
}

void PTree::clear(Node *&node) {
  if (!node) return;
  clear(node->A);
  clear(node->B);
  delete node;
}

bool PTree::isLeaf(Node *node) const {
  return !node->A && !node->B;
}

void PTree::renderLeaf(PNG &img, Node *node) const{
  for (unsigned int y = 0; y < node->height; y++) {
    for (unsigned int x = 0; x < node->width; x++) {
      *img.getPixel(node->upperleft.first + x, node->upperleft.second + y) = node->avg;
    }
  }
}

void PTree::flipHorizontal(Node *node, unsigned int w) {
  if (!node) return;
  node->upperleft.first = w - node->upperleft.first - node->width;
  flipHorizontal(node->A, w);
  flipHorizontal(node->B, w);
}

void PTree::flipVertical(Node *node, unsigned int h) {
  if (!node) return;
  node->upperleft.second = h - node->upperleft.second - node->height;
  flipVertical(node->A, h);
  flipVertical(node->B, h);
}

void PTree::prune(Node *&subtree, double tolerance) {
  if (!subtree || isLeaf(subtree)) return;
  if (isPrunable(subtree, subtree->avg, tolerance)) {
    clear(subtree->A);
    subtree->A = nullptr;
    clear(subtree->B);
    subtree->B = nullptr;
  }
  prune(subtree->A, tolerance);
  prune(subtree->B, tolerance);
}

bool PTree::isPrunable(Node *&node, HSLAPixel &avg, double tolerance) {
  if (isLeaf(node)) return isNodeInTolerance(node, avg, tolerance);
  return isPrunable(node->A, avg, tolerance) && isPrunable(node->B, avg, tolerance);
}

bool PTree::isNodeInTolerance(Node *&node, HSLAPixel &avg, double tolerance) {
  return node->avg.dist(avg) < tolerance;
}

