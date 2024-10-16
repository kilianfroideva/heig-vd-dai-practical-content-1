# Practical content 1 - Bitmap filters

## Overview

This project is a Java-based application that allows users to apply various filters to BMP bitmap images,
such as Black & White, Blurring, Zoom, and Resolution Change. The application uses basic Java libraries to implement
BMP image manipulation. The bitmap data is managed via a custom `BMPFile` class, which uses height, width and a vector
of pixels. Every pixels are defined by his color(RGB). Additionally, the application provides classes `BMPReader` and
`BMPWriter` to read and write BMP files.

### Features

- **Black & White Filter**: Converts the image to grayscale.
- **Blurring Filter**: Applies a blur effect to the image.
- **Zoom Filter**: Zooms in of the image from a specified origin.
- **Resolution Change**: Resizes the image to a new resolution while maintaining its aspect ratio.

## Prerequisites

Make sure you have the following tools installed:

- **Java Development Kit (JDK) 8+**
- **Maven** (for building the project)
- **Git** (for cloning the repository)

## Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/kilianfroideva/heig-vd-dai-practical-content-1
    ```

2. Navigate to the project directory:

    ```bash
    cd .../heig-vd-dai-practical-content-1
    ```

3. Build the project using Maven:

    ```bash
    mvn clean install
    ```

   This will compile the project and download all necessary dependencies.

## Usage

Once the project is built, you can run the application to apply filters to your BMP images.
The application allows you to apply different filters by running specific commands. Below are examples of how to use each filter.

### Black & White Filter

To apply a Black & White (grayscale) filter to a BMP image, use the following command. You can also invert the colors by adding the `--inverted` option.

```bash
java -jar target/practical-work-1-demo-1.0-SNAPSHOT.jar <inputFilePath> <outputFilePath> [-hV] grey -i
```
#### Option
-i or --inverted : Inverts the black and white colors. This option is optional. If omitted, the standard grayscale filter is applied.


#### Examples

grey            |  grey --inverted
:-------------------------:|:-------------------------:
![](BMP_examples/grey/grey.bmp)  |  ![](BMP_examples/grey/grey_inverted.bmp) | 


### Blur Filter

To apply a blur filter to a BMP image, use the following command. You can specify the radius, distance metric, and weight for the blur effect.

```bash
java -jar target/practical-work-1-demo-1.0-SNAPSHOT.jar <inputFilePath> <outputFilePath> blur [-hV] -r=<radius> [-d=<distance_metric>]
[-w=<weight>]
```
#### Options
-r or --radius (required): Specifies the radius of the blur. The value must be positive.
-d or --distanceMetric: Specifies the distance metric (default is 2).
-w or --weight: Specifies the weight of the blur (default is 0).

#### Examples

blur -r=5            |  blur -r=5 -d=-1 -w=2
:-------------------------:|:-------------------------:
![](BMP_examples/blur/blur_r5_d2_w0.bmp)  |  ![](BMP_examples/blur/blur_r5_d-1_w2.bmp) 

### Zoom filter

The Zoom Filter allows you to apply a zoom effect to your bitmap, adjusting the image resolution while focusing on a specific point defined by X and Y percentages of the original image size from the top left corner. Out-ranged pixels are black.

```bash
java -jar target/practical-work-1-demo-1.0-SNAPSHOT.jar <inputFilePath> <outputFilePath> zoom [-hV] -r=<ratio> [-x=<xRatio>] [-y=<yRatio>]
```
#### Options
-r, --ratio: The zoom ratio (required, must be between 0 and 100). A value greater than 1 will zoom in, while a value less than 1 will zoom out.
-x, --xRatio: The X ratio from the original file in percentage (default = 50). Must be between 0 and 100. Defines the horizontal origin for the zoom.
-y, --yRatio: The Y ratio from the original file in percentage (default = 50). Must be between 0 and 100. Defines the vertical origin for the zoom.

#### Examples

zoom -r=50            |  zoom -r=50 -x=10 -y=0
:-------------------------:|:-------------------------:
![](BMP_examples/zoom/zoom_r50.bmp)  |  ![](BMP_examples/zoom/zoom_r25_x10_y0.bmp) | 


### Resolution change

The Resolution Change Filter allows you to reduce the resolution of a bitmap image by specifying a ratio in percentage (100 = origin bitmap).

```bash
java -jar target/practical-work-1-demo-1.0-SNAPSHOT.jar <inputFilePath> <outputFilePath> resolution [-hV] -r=<ratio>
```
#### Options
-r or --ratio : Define the ratio of the decrease in resolution.

#### Examples

resolution -r=50            |  resolution -r=1
:-------------------------:|:-------------------------:
![](BMP_examples/resolution/resolution_r50.bmp) |  ![](BMP_examples/resolution/resolution_r1.bmp)



## Authors

Kilian Froidevaux & Nicolas Bovard



