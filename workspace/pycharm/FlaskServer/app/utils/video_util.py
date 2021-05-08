# -*- coding: utf-8 -*-
import sys

import cv2

reload(sys)
sys.setdefaultencoding('utf8')


def get_video_frame(video_full_path, save_full_path):
    cap = cv2.VideoCapture(video_full_path)  # 返回一个capture对象
    cap.set(cv2.CAP_PROP_POS_FRAMES, 1)  # 设置要获取的帧号
    res, image = cap.read()  # read方法返回一个布尔值和一个视频帧。若帧读取成功，则返回True
    if res:
        cv2.imwrite(save_full_path, image)
    else:
        print res

    cap.release()

# get_video_frame('/Users/yangqiang/Downloads/aa.mp4', '/Users/yangqiang/Downloads/a.jpg')
# print get_covers()
# print get_details('/Users/yangqiang/Downloads/myFolder/3kez')
