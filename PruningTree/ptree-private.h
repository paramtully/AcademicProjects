/*
*  File:        ptree-private.h
*  Description: Private functions for the PTree class
*  Date:        2022-03-06 03:30
*
*               DECLARE YOUR PTREE PRIVATE MEMBER FUNCTIONS HERE
*/

#ifndef _PTREE_PRIVATE_H_
#define _PTREE_PRIVATE_H_

/////////////////////////////////////////////////
// DEFINE YOUR PRIVATE MEMBER FUNCTIONS HERE
//
// Just write the function signatures.
//
// Example:
//
// Node* MyHelperFunction(int arg_a, bool arg_b);
//
/////////////////////////////////////////////////
int numLeaves(Node *node) const;
int size(Node* node) const;
void copy(Node *ori, Node *&cpy);
HSLAPixel buildNodePixel(PNG& im, pair<unsigned int, unsigned int> ul, unsigned int w, unsigned int h);
void clear(Node *&node);
int ciel(double a);
void render(PNG &img, Node* node) const;
bool isLeaf(Node *node) const;
void flipHorizontal(Node *node, unsigned int w);
void flipVertical(Node *node, unsigned int h);
void renderLeaf(PNG &img, Node *node) const;
void prune(Node *&subtree, double tolerance);
bool isPrunable(Node *&node, HSLAPixel &avg, double tolerance);
bool isNodeInTolerance(Node *&node, HSLAPixel &avg, double tolerance);


#endif