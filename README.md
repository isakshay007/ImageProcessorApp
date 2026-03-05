# IME: Image Manipulation and Enhancement

## Overview
This application is built using the MVC (model-view-controller) architecture and supports various image processing operations, including format conversion, basic transformations, color adjustments, and filtering effects. Below is a detailed breakdown of each package and its components.

The codebase applies three Gang of Four design patterns to improve modularity and extensibility:

- **Strategy Pattern** -- Image transformation algorithms (blur, sharpen, sepia, greyscale, color correction) are encapsulated in interchangeable strategy classes behind a common `ImageOperation` interface, eliminating switch statements in the model.
- **Factory Pattern** -- Image loading is delegated to an `ImageLoaderFactory` that returns the correct `ImageLoader` implementation (PPM or standard formats) based on file extension, replacing the if/else chain in `ImageIOHandler`.
- **Command Pattern** -- Each controller action is encapsulated in its own command class implementing `ImageCommand`, registered in a map for lookup, replacing the giant switch block in `ImageControllerImpl`.

---

## controller Package

### ImageController.java (Interface)
**Purpose:** Defines the contract for processing text commands and running scripts.
**Responsibilities:**
- Process individual command strings
- Execute script files containing sequences of commands

### ImageControllerImpl
**Purpose:** Routes parsed commands to the appropriate `ImageCommand` object via a map lookup.
**Responsibilities:**
- Initializes a `Map<String, ImageCommand>` of all supported commands in the constructor
- Tokenizes input, looks up the command, and delegates execution
- Provides `runScript()` for batch processing from a file

### controller.command Package (Command Pattern)

| Class | Description |
|---|---|
| `ImageCommand` | Interface with `void execute(StringTokenizer)` |
| `LoadCommand` | Loads an image from a file path |
| `SaveCommand` | Saves an image to a file path |
| `ChannelCommand` | Shared command for `red-component`, `green-component`, `blue-component` |
| `ComponentCommand` | Shared command for `value-component`, `intensity-component`, `luma-component` |
| `FlipCommand` | Flips an image horizontally or vertically |
| `BrightenCommand` | Adjusts image brightness |
| `BasicOperationCommand` | Shared command for `blur`, `sharpen`, `sepia` (with optional mask support) |
| `GreyscaleCommand` | Converts an image to greyscale by component |
| `RgbSplitCommand` | Splits an image into its RGB channels |
| `RgbCombineCommand` | Combines RGB channels into a single image |
| `HistogramCommand` | Generates a histogram image |
| `ColorCorrectCommand` | Applies color correction |
| `LevelsAdjustCommand` | Adjusts black, mid, and white level points |
| `SplitCommand` | Applies a split-view operation |
| `CompressCommand` | Compresses an image via Haar wavelet transform |
| `DownscaleCommand` | Downscales an image to new dimensions |
| `RunScriptCommand` | Executes a script file (holds a reference to the controller) |

---

## model Package

### ImageModel (Interface)
**Purpose:** Abstracts the various image processing capabilities.
**Responsibilities:**
- Image loading and saving
- Channel and component visualization (with optional mask support)
- Transformations: flip, brighten, blur, sharpen, sepia, greyscale
- RGB split and combine
- Histogram generation
- Color correction and levels adjustment
- Split-view operations
- Compression and downscaling

### Image
**Purpose:** Immutable representation of a digital image with separate RGB channels.
**Responsibilities:**
- Stores width, height, and 2D arrays for red, green, and blue channels

### ImageModelImpl
**Purpose:** Implementation of `ImageModel` that manages image storage and processing.
**Responsibilities:**
- Maintains `Map<String, Image>` for original images and `Map<String, BufferedImage>` for processed images
- Holds a `Map<String, ImageOperation>` strategy map (registered in the constructor) for blur, sharpen, sepia, greyscale, and color correction
- Uses `strategies.get(operationName).apply(image)` in `splitOperation()` instead of inline switch logic
- Delegates kernel values for masked blur/sharpen to `BlurOperation.getKernel()` and `SharpenOperation.getKernel()`
- Retains all helper methods for masked operations, histogram rendering, levels adjustment, compression, and downscaling

### model.strategy Package (Strategy Pattern)

| Class | Description |
|---|---|
| `ImageOperation` | Interface with `Image apply(Image image)` |
| `BlurOperation` | 3x3 Gaussian blur; exposes `static getKernel()` |
| `SharpenOperation` | 5x5 sharpen filter; exposes `static getKernel()` |
| `SepiaOperation` | Applies sepia tone transformation |
| `GreyscaleOperation` | Converts to greyscale via RGB averaging |
| `ColorCorrectOperation` | Aligns histogram peaks across channels |

### ImageIOHandler
**Purpose:** Bridges raw image data and file representations for loading and saving.
**Responsibilities:**
- `loadImage()` delegates to `ImageLoaderFactory.getLoader(filePath).load(filePath)`
- Saving remains unchanged (PPM via `ImageParser`, standard formats via `ImageIO`)

### model.factory Package (Factory Pattern)

| Class | Description |
|---|---|
| `ImageLoader` | Interface with `Image load(String filePath)` |
| `PPMLoader` | Loads PPM P3 images using `FileHandler` and `ImageParser` |
| `StandardImageLoader` | Loads PNG/JPG/JPEG via `ImageIO` and `BufferedImageConverter` |
| `ImageLoaderFactory` | Static `getLoader(String filePath)` returns the correct loader by extension |

### Supporting Classes
- **`FileHandler`** -- Reads/writes files via `Scanner` and `PrintWriter`
- **`ImageParser`** -- Parses and writes PPM P3 format
- **`BufferedImageConverter`** -- Converts between `Image` and `BufferedImage`
- **`ImageUtils`** -- Static `clamp(int)` utility to keep pixel values in [0, 255]

---

## view Package

### ImageView (Interface)
**Purpose:** Rendering messages to the user and obtaining user input.
**Responsibilities:**
- Render messages for user feedback and information about operations
- Retrieve user input for file paths, commands, or parameters

### ImageViewImpl
**Purpose:** Console implementation of `ImageView` using `System.in` and `System.out`.

### ImageViewGUI
**Purpose:** Swing-based GUI with image display, histogram panel, operation selector, and split-view toggle.

---

## Testing Package

### ImageControllerImplTest.java
**Purpose:** Tests that controller commands produce the correct console output across all formats.
**Tests:**
- Load and save for PPM, PNG, JPG
- All transformations: brighten, flip, blur, sharpen, sepia, greyscale, compress, downscale
- Split-view operations
- Mask-based operations for blur, sharpen, sepia, greyscale, and channel/component visualization
- Script execution via `run` command

### ImageExceptionTest.java
**Purpose:** Ensures appropriate exceptions are thrown for edge cases.
**Tests:**
- Unsupported file formats
- Operations on missing images
- Invalid parameters (e.g., out-of-range levels, negative compression)

### ImageModelImplTest.java
**Purpose:** Tests pixel-level correctness of model operations against reference images.
**Tests:**
- Basic transformations (flip, brighten, darken)
- Filters (blur, sharpen, sepia, greyscale)
- RGB split and combine
- Histogram, color correction, and levels adjustment
- Split-view operations for all supported algorithms
- Compression at multiple levels (10%, 50%, 95%)
- Downscaling
- Masked operations for blur, sharpen, sepia, and component visualization

---

## Installation
1. Run the `Main` class.
2. Use `-text` for interactive text mode, `-file <path>` to run a script, or no arguments for the GUI.

## Script Commands

| Command | Syntax | Example |
|---|---|---|
| `load` | `load <file-path> <image-name>` | `load Images/Landscape.png l1` |
| `save` | `save <file-path> <image-name>` | `save res/output.png l1` |
| `brighten` | `brighten <amount> <image-name> <dest>` | `brighten 20 l1 l1-brighter` |
| `flip` | `flip <horizontal\|vertical> <image-name> <dest>` | `flip horizontal l1 l1-flipped` |
| `red-component` | `red-component <image-name> <dest>` | `red-component l1 l1-red` |
| `green-component` | `green-component <image-name> <dest>` | `green-component l1 l1-green` |
| `blue-component` | `blue-component <image-name> <dest>` | `blue-component l1 l1-blue` |
| `value-component` | `value-component <image-name> <dest>` | `value-component l1 l1-value` |
| `luma-component` | `luma-component <image-name> <dest>` | `luma-component l1 l1-luma` |
| `intensity-component` | `intensity-component <image-name> <dest>` | `intensity-component l1 l1-intensity` |
| `greyscale` | `greyscale <component> <image-name> <dest>` | `greyscale luma l1 l1-grey` |
| `sepia` | `sepia <image-name> <dest>` | `sepia l1 l1-sepia` |
| `blur` | `blur <image-name> <dest>` | `blur l1 l1-blur` |
| `sharpen` | `sharpen <image-name> <dest>` | `sharpen l1 l1-sharp` |
| `rgb-split` | `rgb-split <image-name> <red> <green> <blue>` | `rgb-split l1 l1-r l1-g l1-b` |
| `rgb-combine` | `rgb-combine <dest> <red> <green> <blue>` | `rgb-combine l1-combined l1-r l1-g l1-b` |
| `histogram` | `histogram <image-name> <dest>` | `histogram l1 l1-hist` |
| `color-correct` | `color-correct <image-name> <dest>` | `color-correct l1 l1-cc` |
| `levels-adjust` | `levels-adjust <black> <mid> <white> <image-name> <dest>` | `levels-adjust 20 128 230 l1 l1-la` |
| `compress` | `compress <percentage> <image-name> <dest>` | `compress 50 l1 l1-compressed` |
| `downscale` | `downscale <width> <height> <image-name> <dest>` | `downscale 200 100 l1 l1-small` |
| `split` | `split <op> <image-name> <dest> <percent> [params]` | `split blur l1 l1-split 50` |
| `run` | `run <script-path>` | `run scripts/commands.txt` |

Mask-based operations are supported by providing an optional mask image name between the source and destination (e.g., `blur l1 mask l1-blurred`).

---

## IMAGE CITATION
- Images taken by [Pranav Viswanathan](https://www.flickr.com/photos/199542081@N07/albums/with/72177720312735513)