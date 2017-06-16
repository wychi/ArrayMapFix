# ArrayMapFix
Demo a solution to fix java.lang.ClassCastException in ArrayMap.

## patch ArrayMap in support-v4

The workaround implemented here is to add a synchronized block to protect mArray.

Then use gradle Transform API to patch support-v4 library.
