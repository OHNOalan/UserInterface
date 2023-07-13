Alan Lee <br>
a372lee <br>
kotlinc-jvm 1.8.20 (jre 18.0.2) <br>
macOS Ventura 13.2 <br>

Notes:
1. Using ImageIO.read(File) to check if a file is an image, if it doesn't throw exceptions, then we treat file as a valid image.
2. Changing from Tile mode to Cascade mode, it recovers the position/scale/rotate properties.
3. File adding in tile mode will locate in origin(top-left cornor) after switching to cascade mode.

Cite: 
util/Tween from Animation/Tween

Images: <br>
CS349 Images & Sound 