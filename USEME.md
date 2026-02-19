# IMAGE PROCESSING APPLICATION - USER GUIDE

This document outlines all supported commands and their usage in our image processing application.

## BASIC COMMANDS

1. `load <image-path> <image-name>`
   - Loads an image from the specified path and assigns it the given name
   - Example: `load images/beach.jpg beach1`

2. `save <image-path> <image-name>`
   - Saves the image with the given name to the specified path
   - Example: `save processed/beach-bright.png beach1-bright`

## IMAGE OPERATIONS

1. `brighten <increment> <image-name> <dest-image-name>`
   - Brightens/darkens image by the specified increment
   - Example: `brighten 50 beach1 beach1-bright`
   - Note: Negative values will darken the image

2. `horizontal-flip <image-name> <dest-image-name>`
   - Flips image horizontally
   - Example: `horizontal-flip beach1 beach1-horizontal`

3. `vertical-flip <image-name> <dest-image-name>`
   - Flips image vertically
   - Example: `vertical-flip beach1 beach1-vertical`

## COLOR COMPONENTS

1. `red-component <image-name> <dest-image-name>`
   - Extracts red component
   - Example: `red-component beach1 beach1-red`

2. `green-component <image-name> <dest-image-name>`
   - Extracts green component
   - Example: `green-component beach1 beach1-green`

3. `blue-component <image-name> <dest-image-name>`
   - Extracts blue component
   - Example: `blue-component beach1 beach1-blue`

4. `value-component <image-name> <dest-image-name>`
   - Creates grayscale using maximum RGB value
   - Example: `value-component beach1 beach1-value`

5. `luma-component <image-name> <dest-image-name>`
   - Creates grayscale using weighted RGB values
   - Example: `luma-component beach1 beach1-luma`

6. `intensity-component <image-name> <dest-image-name>`
   - Creates grayscale using average RGB values
   - Example: `intensity-component beach1 beach1-intensity`

## ADVANCED OPERATIONS

1. `blur <image-name> <dest-image-name>`
   - Applies Gaussian blur
   - Example: `blur beach1 beach1-blur`

2. `sharpen <image-name> <dest-image-name>`
   - Sharpens the image
   - Example: `sharpen beach1 beach1-sharp`

3. `greyscale <image-name> <dest-image-name>`
   - Converts to grayscale using luma
   - Example: `greyscale beach1 beach1-grey`

4. `sepia <image-name> <dest-image-name>`
   - Applies sepia tone
   - Example: `sepia beach1 beach1-sepia`

## RGB OPERATIONS

1. `rgb-split <image-name> <red-name> <green-name> <blue-name>`
   - Splits image into RGB components
   - Example: `rgb-split beach1 beach1-r beach1-g beach1-b`

2. `rgb-combine <dest-image-name> <red-name> <green-name> <blue-name>`
   - Combines RGB components into one image
   - Example: `rgb-combine beach1-combined beach1-r beach1-g beach1-b`

## NEW FEATURES

1. `compress <percentage> <image-name> <dest-image-name>`
   - Compresses image by specified percentage (0-100)
   - Example: `compress 50 beach1 beach1-compressed`

2. `histogram <image-name> <dest-image-name>`
   - Generates histogram visualization
   - Example: `histogram beach1 beach1-histogram`

3. `color-correction <image-name> <dest-image-name>`
   - Performs color correction
   - Example: `color-correction beach1 beach1-corrected`

4. `levels-adjust <black> <mid> <white> <image-name> <dest-image-name>`
   - Adjusts color levels (values between 0-255)
   - Example: `levels-adjust 20 128 255 beach1 beach1-adjusted`

## SPLIT PREVIEW

Any operation can be split-previewed using the split keyword:
`<operation> <image-name> <dest-image-name> split <percentage>`
Example: `blur beach1 beach1-split-blur split 50`

## SCRIPTING

1. `run-script <script-path>`
   - Runs commands from a script file
   - Example: `run-script scripts/process-beach.txt`

2. `exit`
   - Exits the application

## SUPPORTED FILE FORMATS

- PNG (.png)
- JPG/JPEG (.jpg, .jpeg)
- PPM (P3 format) (.ppm)

## IMPORTANT NOTES

1.Image Loading Requirement: An image must be loaded into the model before any operations can be applied to it.
2.Immutability of Original Image: All image operations (e.g., transformations, filters, adjustments)generate new images. The original image remains unchanged after the operation.
3.File Path Flexibility: File paths for loading and saving images can be either relative or absolute. Ensure the path is correct for successful loading/saving.
4.Error Handling: Invalid commands, parameters, or file paths will trigger error messages. Ensure inputs are valid to avoid interruptions.
5.Levels Adjustment: When performing levels adjustment, the parameters for black, mid, and white must each be between 0 and 255, with black < mid < white.
6.Compression Constraints: The compression percentage must be between 0 and 100. Values outside this range will result in an error.
7.Split Preview Percentage: The preview percentage for split operations must be between 0 and 100, specifying how much of the operation's effect is applied in preview mode.
8.Supported Formats: The current operations are specifically designed for PNG image format. Ensure the images loaded are in a supported format to avoid compatibility issues.
9.Undoing Changes: The image model does not support undoing operations; once an operation is applied and saved, it cannot be reverted unless you have saved the original image separately.
10.Memory Considerations: For large images, memory usage may increase significantly during operations such as blurring, sharpening, or compression. It's recommended to use images with appropriate resolution based on the available system memory.

## EXAMPLE WORKFLOW

```
load images/koala.jpg koala1
brighten 30 koala1 beach1-bright
blur koala1-bright koala1-blur
sepia koala1-blur koala1-final
save processed/koala-final.jpg koala1-final
```