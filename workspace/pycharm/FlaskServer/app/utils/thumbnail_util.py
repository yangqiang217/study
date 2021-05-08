# -*- coding: utf-8 -*-
import os
from PIL import Image
from app.const import ROOT_DIR_XUEMEI, THUMBNAIL_DIR_NAME, DS_Store, THUMBNAIL_COUNT_FLAG
import time
from shutil import copyfile
import threading
from app.basemodel.Detail import detail_sort_str


def create_thumbnail(thumbnail_width, small=False):
    thumbnail_path = os.path.join(ROOT_DIR_XUEMEI, THUMBNAIL_DIR_NAME, str(thumbnail_width))#/User/../.sysc/.thumbnail

    if not os.path.exists(thumbnail_path):
        os.mkdir(thumbnail_path)

    group_list = os.listdir(ROOT_DIR_XUEMEI)

    if THUMBNAIL_DIR_NAME in group_list:
        group_list.remove(THUMBNAIL_DIR_NAME)
    if DS_Store in group_list:
        group_list.remove(DS_Store)

    #处理group改名或删除了thumbnail还在的情况
    thumbnail_list = os.listdir(thumbnail_path)
    if DS_Store in thumbnail_list:
        thumbnail_list.remove(DS_Store)
    for i in range(0, len(thumbnail_list)):
        tni = thumbnail_list[i]
        if tni not in group_list:
            print("-----------------------" + tni + " not in ori list, please delete")

    group_list_divided = list_split(group_list, 1)#分份给个线程
    # for idx in range(0, len(group_list_divided)):
    #     print("----------" + str(idx))
    #     g = group_list_divided[idx]
    #     for j in range(0, len(g)):
    #         print("第" + str(idx) + "组：" + g[j] + ", " + str(j))


    for i in range(0, len(group_list_divided)):
        t = threading.Thread(target=create_per_thread, args=(group_list_divided[i], thumbnail_path, thumbnail_width, small))
        t.start()


def list_split(arr, group_count):
    total = len(arr)
    size = 0
    for i in range(0, total + 1):
        if i * group_count >= total:
            size = i
            break

    return [arr[i:i+size] for i in range(0, len(arr), size)]


def create_per_thread(group_list, thumbnail_path, thumbnail_width, small):
    thread_name = threading.current_thread().getName()

    for i in range(0, len(group_list)):
        # t = time.time()
        group_name = group_list[i]#aa_xx_2019-09-09
        print("start make thumbnail of group " + str(i) + ": " + group_name + " " + thread_name)
        if group_name == THUMBNAIL_DIR_NAME or group_name == DS_Store:
            continue

        group_ori_path = os.path.join(ROOT_DIR_XUEMEI, group_name)#/User/../.sysc/aa_xx_2019-09-09
        group_thumbnail_path = os.path.join(thumbnail_path, group_name)#/User/../.sysc/.thumbnail/aa_xx_2019-09-09

        if not os.path.exists(group_thumbnail_path):
            os.mkdir(group_thumbnail_path)

        img_list = os.listdir(group_ori_path)
        if DS_Store in img_list:
            img_list.remove(DS_Store)

        rounds = len(img_list)
        if small:
            img_list.sort(detail_sort_str)
            rounds = 1
            # cover 不知道图片数量了
            count_file_path = os.path.join(group_thumbnail_path, str(len(img_list)) + THUMBNAIL_COUNT_FLAG)
            if not os.path.exists(count_file_path):
                open(count_file_path, "w")

        for j in range(0, rounds):
            img_name = img_list[j]  #1.jpg
            if img_name == DS_Store or img_name == ".jpg":
                continue

            img_ori_path = os.path.join(group_ori_path, img_name)#/User/../.sysc/aa_xx_2019-09-09/1.jpg
            img_thumbnail_path = os.path.join(group_thumbnail_path, img_name)#/User/../.sysc/.thumbnail/aa_xx_2019-09-09/1.jpg

            if os.path.exists(img_thumbnail_path):
                continue

            try:
                img = Image.open(unicode(img_ori_path, 'utf8'))
                if thumbnail_width >= img.width:
                    # 直接拷贝过来
                    copyfile(img_ori_path, img_thumbnail_path)
                    print("    copied")
                    continue

                thumbnail_height = thumbnail_width * img.height / img.width
                size = (thumbnail_width, thumbnail_height)
                img.thumbnail(size)
                img.save(img_thumbnail_path)

                print("    made")
            except IOError:
                is_created = os.path.exists(img_thumbnail_path)
                print("-------cannot create thumbnail for: " + img_ori_path + ", created: " + str(is_created) + ", " + thread_name)
                if is_created:
                    os.remove(img_thumbnail_path)

        # print("one group cost: " + str(time.time() - t) + " " + thread_name)


create_thumbnail(360, True)
# create_thumbnail(1080)
