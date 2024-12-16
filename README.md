# IME: Image Manipulation and Enhancement

## Overview
This application is built using the MVC (model-view-controller) architecture and supports various image processing operations, including format conversion, basic transformations, color adjustments, and filtering effects. Below is a detailed breakdown of each package and its components.
In the second iteration, we added more functionalities to the application. This includes functionalities like image compression using the Haar Wavelet Transform, histogram generation, color correction and levels adjustment.

## controller Package

### ImageController.java (Interface)
**Purpose:** Main controller class implementing the command pattern.  
**Responsibilities:**
- Manages operation mappings
- Handles image loading/saving
- Executes image transformations
- Routes commands between the model and view

### ImageControllerImpl

**Purpose:** Handle commands related to image processing
**Responsibilities**
- Command Processing
- Error Handling
- Image Operations

## model Package

### ImageModel (Interface)

**Purpose:** Abstract the various image processing capabilities.  
**Responsibilities:**
- Image Loading and Saving
- Image Visualization
- Image Transformation
- Color Channel Operations
- Greyscale Conversion
- Generate histograms for images.
- Perform color correction based on histogram alignment.
- Adjust image levels (black, mid, and white points).
- Perform image compression.

### Image

**Purpose:**  Digital image with RGB color channels
**Responsibilities**
- Width and Height
- RGB Channels
- Encapsulation

### ImageModelImpl

**Purpose:** Implementation of the ImageModel interface for managing image loading, saving, and processing operations.
**Responsibilities:**

- Load images in various formats (PPM, PNG, JPG).
- Save images in PPM, PNG, and JPG formats.
- Visualize specific color channels of images.
- Apply transformations like flipping, brightening, blurring, and sharpening.
- Convert images to sepia tone and greyscale.
- Split and combine RGB channels
- Adjust image levels (black, mid, white points).
- Color correction using peak analysis.
- Histogram generation and visualization.
- Split-image processing for specific operations (e.g., blur, sharpen, sepia, etc.).
- Image compression (via Haar transform).

### ImageIOHandler

**Purpose:** Serves as a utility to bridge between raw image data and file representations, enabling image manipulation and storage.

**Responsibilities:**

- Load images in PPM format by parsing RGB values and creating an Image object.
- Load standard image formats (e.g., PNG, JPG) and convert them to an Image object representation.
- Save images in PPM format by writing RGB values to a file in the expected format.
- Save images in standard formats (PNG, JPG) by converting Image data to a BufferedImage and writing it to a file.
- Validate file formats to ensure compatibility (supports only PNG and JPG for standard image saving).

**Why was it added?**
- Centralized Image Management:
- Consolidates all image input/output operations in one place, making the codebase easier to manage and extend.
- Format Flexibility:
- Abstracts details, allowing seamless interaction with images regardless of format, which is essential for applications that need to support multiple image sources.
- Simplified Error Handling and Validation:
- Ensures robust error management, making it easier for other parts of the application to interact with images without worrying about format-specific issues.
- Reusable and Extensible Structure:
  Provides reusable methods for loading and saving images, which can be leveraged by various parts of an application without duplicating code.


## view

### ImageView (Interface)

**Purpose:** Rendering messages to the user and obtaining user input in the context of image processing applications.
**Responsibilities:**

- Render messages for user feedback and information about operations.
- Retrieve user input for file paths, commands, or parameters.

### ImageViewImpl

**Purpose:** Provides a concrete implementation of the ImageView interface, facilitating user interaction by rendering messages and obtaining input in image processing applications.
**Responsibilities:**

- Implement methods to render messages to the user.
- Implement methods to retrieve user input from the console.

## Testing Package

### ImageControllerTest.java
**Purpose:** Tests error handling in the application.  
**Tests:**
- Load and Save Image for All Formats
- File Loading and Saving Across Formats
- Command Processing
- Console Output Validation


### ImageExceptionTest.java
**Purpose:** To ensure that appropriate exceptions are thrown in edge cases, such as when the image file format is unsupported or when operations are attempted on missing images.  
**Tests:**
- Supported Image Formats
- Various Image Manipulations
- Exception Handling for Invalid Operations

### ImageModelImplTest.java
**Purpose:** Tests PNG-specific operations.  
**Tests:**
- Basic transformations
- Image enhancement
- Filters and effects
- Component visualization
- Image compression and histogram generation
- Image processing with various operations applied in sequence

### Installation
- Run Main class.
- Type run-script and paste the script location.

### Script commands with examples:

- load
- Syntax : <file-path> <image-name>
- Eg: load Images/Landscape.png l1
- save
- Syntax : <file-path> <image-name>
- Eg: save res/landscape-red-component.png l1-red-component
- brighten
- Syntax :  <factor> <image-name> <dest-image-name>
- Eg: brighten 20 l1 l1-brighter
- horizontal-flip
- Syntax : <image-name> <dest-image-name>
- Eg: horizontal-flip l1 l1-horizontal-flip
- vertical-flip
- Syntax : <image-name> <dest-image-name>
- Eg: vertical-flip l1 l1-vertical-flip
- rgb-split
- Syntax : <image-name> <red-image> <green-image> <blue-image>
- Eg: rgb-split l1 l1-red-split l1-green-split l1-blue-split
- rgb-combine
- Syntax : <dest-image-name> <red-image> <green-image> <blue-image>
- Eg: combine l1-combine l1-red-split l1-green-split l1-blue-split
- value-component
- Syntax : <image-name> <dest-image-name>
- Eg: value-component l1 l1-value
- luma-component
- Syntax : <image-name> <dest-image-name>
- Eg: luma-component l1 l1-luma
- intensity-component
- Syntax : <image-name> <dest-image-name>
- Eg: intensity-component l1 l1-intensity
- greyscale
- Syntax : <image-name> <dest-image-name>
- Eg: greyscale l1 l1-greyscale
- sepia
- Syntax : <image-name> <dest-image-name>
- Eg: save res/landscape-sepia.png l1-sepia
- blur
- Syntax : <image-name> <dest-image-name>
- Eg: blur l1 l1-blur
- sharpen
- Syntax : <image-name> <dest-image-name>
- Eg: sharpen l1 l1-sharper
- Apply a blurred effect to the left half of the image using a 50% split
- Syntax: <image-name> <dest-image-name> <amount>
- Eg: split blur download-png dest-blur.png 50
- Apply a sharpen effect to the left half of the image using a 50% split
- Syntax: <image-name> <dest-image-name> <amount>
- Eg: split sharpen download-png dest-sharpen.png 50
- Apply sepia to the left half of the image using a 50% split
- Syntax: <image-name> <dest-image-name> <amount>
- Eg: split sepia download-png dest-sepia.png 50
- Apply greyscale to the left half of the image using a 50% split
- Syntax: <image-name> <dest-image-name> <amount>
- Eg: split greyscale download-png dest-greyscale.png 50
- Apply color correction to the left half of the image using a 50% split
- Syntax: <image-name> <dest-image-name> <amount>
- Eg: split colorcorrect download-png dest-colorcorrect.png 50
- Adjust levels on the left half of the image with a split at 50%, using black, mid, and white points
- Syntax: <image-name> <dest-image-name> <amount>
- Eg: split levels download-png dest-levels.png 50 0 128 255
- Histogram visualization for the PNG image
- Syntax: histogram <image-name> <dest-image-name>
- Eg:histogram download-png download-histogram-png
- color correction
- Syntax: color-correction <image-name> <dest-image-name>
- Eg: color-correct download-png colorcorrected-png
- Adjust levels of the PNG image with specified black, mid, and white points
- Syntax: color-correction <image-name> <dest-image-name>
- Eg: levels-adjust 100 128 234 download-png levelsadjust.png
- Compress the image by 10% using Haar wavelet compression
- Syntax: compress <percentage> <image-name> <dest-image-name>
- Eg: compress 10 koala-square compressed_image_10.png
- Compress the image by 50% using Haar wavelet compression
- Syntax: compress <percentage> <image-name> <dest-image-name>
- Eg: compress 50 koala-square compressed_image_50.png
- Compress the image by 95% using Haar wavelet compression
- Syntax: compress <percentage> <image-name> <dest-image-name>
- Eg: compress 95 koala-square compressed_image_95.png
- run-script
- Syntax : <script-file-path>
- Eg: run-script Images/PNGScript.txt


## IMAGE CITATION ##
- Images taken by [Pranav Viswanathan](https://www.flickr.com/photos/199542081@N07/albums/with/72177720312735513)