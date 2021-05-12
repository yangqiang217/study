//
//  main.cpp
//  OpenCV
//
//  Created by YangQiang on 2021/4/30.
//

#include <iostream>
#include <fstream>
#include <opencv2/opencv.hpp>
using namespace cv;
using namespace std;
using std::string;


void simpleTransformation(const Mat &image) {
    namedWindow("Example2_5-in");
    namedWindow("Example2_5-out");
    
    imshow("Example2_5-in", image);
    Mat out;
    GaussianBlur(image, out, Size(5, 5), 3, 3);
    GaussianBlur(out, out, Size(5, 5), 3, 3);
    imshow("Example2_5-out", out);
    
    waitKey(0);
}


void showImage() {
    string path = "/Users/yangqiang/Downloads/a.jpg";
    Mat image = imread(path);
    namedWindow("origin");
    simpleTransformation(image);
//    imshow("origin", image);
    
//    Mat gray;
//    cvtColor(image, gray, COLOR_RGBA2GRAY);
//    namedWindow("gray window", 100);
//    imshow("window title", gray);
    waitKey(0);
}


int g_slider_position = 0;
int g_run = 1, g_dontset = 0;
VideoCapture g_cap;

void onTrackbarSlide(int pos, void *) {
    g_cap.set(CAP_PROP_POS_FRAMES, pos);
    if (!g_dontset) {
        g_run = 1;
    }
    g_dontset = 0;
}
void showVideo() {
    namedWindow("window_name");
    g_cap.open("/Users/yangqiang/Downloads/a.mp4");
    
    int frames = (int) g_cap.get(CAP_PROP_FRAME_COUNT);
    int tmpw = (int) g_cap.get(CAP_PROP_FRAME_WIDTH);
    int tmph = (int) g_cap.get(CAP_PROP_FRAME_HEIGHT);
    cout << "video has " << frames << " frames of dimensions(" << tmpw << ", " << tmph << ")" << endl;
    
    createTrackbar("track_bar_name", "window_name11", &g_slider_position, frames, onTrackbarSlide);
    
    Mat frame;
    for (;;) {
        if (g_run != 0) {
            g_cap >> frame;
            if (frame.empty()) {
                break;
            }
            int curr_pos = (int) g_cap.get(CAP_PROP_POS_FRAMES);
            g_dontset = 1;
            
            setTrackbarPos("track_bar_name", "window_name", curr_pos);
            imshow("window_name", frame);
            
            g_run -= 1;
        }
        
        char c = (char) waitKey(10);
        if (c == 's') {
            g_run = 1;
            cout << "Single step, run = " << g_run << endl;
        }
        if (c == 'r') {
            g_run = -1;
            cout << "Run mode, run = " << g_run << endl;
        }
        if (c == 'b') {
            break;
        }
    }
}

void downsampling() {
    Mat img1, img2;
    namedWindow("exp1");
    namedWindow("exp2");
    img1 = imread("/Users/yangqiang/Downloads/a.jpg");
    img2 = imread("/Users/yangqiang/Downloads/b.png");
    pyrDown(img1, img2);
//    imshow("exp1", img1);
    imshow("exp2", img2);
    
    waitKey(0);
}

int main(int argc, const char * argv[]) {
//    showImage();
//    showVideo();
    downsampling();
    return 0;
}
