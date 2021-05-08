# -*- coding: utf-8 -*-
from __future__ import print_function

import os, sys
from PIL import Image
from app.const import ROOT_DIR_XUEMEI
import time
import datetime
import thread
import threading
import time

mod = "shit"
name = "shsit_sdjf"
print(mod in name)


# print(os.path.exists("/Users/yangqiang/workspace/android_studio/.mm/.syscs"))

#
# img_ori_path = '/Users/yangqiang/workspace/android_studio/.mm/.sysc/妖妖_三生琥珀，一世琉璃_2018-01-16/8.JPG'
# img_thumbnail_path = '/Users/yangqiang/workspace/android_studio/.mm/.sysc/.thumbnail/1080/妖妖_三生琥珀，一世琉璃_2018-01-16/8.JPG'
# try:
#     img = Image.open(unicode(img_ori_path, 'utf8'))
#     size = (360, 240)
#     img.thumbnail(size)
#     img.save(img_thumbnail_path)
# except IOError:
#     print("cannot create thumbnail for", img_ori_path)

#
# def shit(max):
#     print("in shit")
#     time.sleep(1)
#     print(threading.current_thread().getName() + str(max))
#
# t1 = threading.Thread(target=shit, args=(100,))
# t1.start()
# t2 = threading.Thread(target=shit, args=(1,))
# t2.start()