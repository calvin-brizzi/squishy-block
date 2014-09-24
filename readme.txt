			Squishy Block
		       (Working title)
		      A game in progress

THIS IS A ARCHIVED VERSION WITH PIXEL-PERFECT COLLISION

This is a small preview of what is to come. Nothing much happening yet, but you can
easily see that collision detection works.

QuickStart guide:
In a terminal run "make run"
Arrows control the player (little blue man)
WASD controls the tetronimo (tetris-like block)
SPACE views the bounding polygons

Player collides with things and bounces back. The other things print "collision" to
the terminal but don't have any logic to react. 

I decided to do my own thing from scratch rather than the supplied blocky thing.
Learnt a lot.

So, now for marks:

First 65%:

Pixel checking is implemented by generating a mask when the object is created. This 
only works for resources under 64 pixels wide, but that's ok, because mine are all
under 60 pixels. If bigger resources were needed, the code would need to be modified.

CollisionChecker does just that, using the relative position of the bottom left corner
of each sprite, it shifts the bits and compares the correct rows by &ing the two longs.

Second 35%:

To avoid the n^2 comparisons, I used a grid. A grid works well in this situation as 
there are many moving parts and they are small, so they usually fit in to grid cells.
It is also preferable to a Quadtree as it happens not to be a pain in the bloody ass
to code (I coded a Quadtree first, 2 hours I will never see again) and implement.

So objects are put into grid cells and we only check collisions in grid cells with 
more than one object. Then, to further save time we only compare each object to the 
following objects in the list (so the first to all the others, then the second to 
all following objects etc)

This greatly reduces comparisons with a to O(n) in a quite bad case (there are a few 
cases were it will be 4*n comparisons, but it's such an improbable edge case it's 
not worth considering).

Third 15%:

The shape that benefits from it is bound by a polygon that "hugs" it nicely. This
reduces the number of pixel collision tests we do. We also create polygons for the 
other shapes to allow us to use convenient methods from libgdx.

Conclusion:

Pixel perfect collisions probably won't be part of my final game. But the grid and 
polygon optimazations probably will.

Known bugs that were beyond the scope of this assingment:

The tetronimo can go anywhere, needs to be bound to the playing area.
If a player leaves the sides while not pressing a direction button, he will stay out.
Both easy to fix, but I don't need to, and I probably want the behaviour in the 
final game.
