# -*- coding: utf-8 -*-
import os
import sys

from PIL import Image

from app.MyEncoder import MyEncoder
from app.basemodel.Cover import Cover
from app.basemodel.Detail import Detail
from app.basemodel.Detail import detail_sort
from app.responsemodel.ResponseModel import CoverResponse
from app.responsemodel.ResponseModel import DetailResponse
from app.const import ROOT_MIX_TEMP, DS_Store
from app.utils import video_util

reload(sys)
sys.setdefaultencoding('utf8')


def get_covers(root_dir, is_sort=False):
    cover_list = []

    group_list = os.listdir(root_dir)
    for index in range(0, len(group_list)):
        group_name = group_list[index]
        group_path = os.path.join(root_dir, group_name)

        if DS_Store in group_name:
            os.remove(group_path)
            continue

        cover = Cover()
        cover.title = group_name
        if os.path.isdir(group_path):
            cover.is_dir = 1
        else:
            cover.is_dir = 0

            frame_path = os.path.join(ROOT_MIX_TEMP, group_path, '.jpg')
            if not os.path.exists(frame_path):
                video_util.get_video_frame(group_path, frame_path)

            cover.img_path = group_name
            img = Image.open(unicode(frame_path, 'utf8'))
            cover.img_width = img.width
            cover.img_height = img.height


        detail_list = os.listdir(group_path)
        for i in range(0, len(detail_list)):
            img_name1 = str(i) + '.jpg'
            img_name2 = str(i) + '.JPG'
            img_name3 = str(i) + '.png'
            img_name4 = str(i) + '.PNG'
            check_path1 = group_name + '/' + img_name1
            check_path2 = group_name + '/' + img_name2
            check_path3 = group_name + '/' + img_name3
            check_path4 = group_name + '/' + img_name4
            if os.path.exists(os.path.join(root_dir, check_path1)):
                cover.img_path = check_path1
                break
            if os.path.exists(os.path.join(root_dir, check_path2)):
                cover.img_path = check_path2
                break
            if os.path.exists(os.path.join(root_dir, check_path3)):
                cover.img_path = check_path3
                break
            if os.path.exists(os.path.join(root_dir, check_path4)):
                cover.img_path = check_path4
                break

            cover.img_path = ''

        titlepart = group_name.split('_')
        if len(titlepart) == 0:
            continue
        elif len(titlepart) == 1:
            cover.title = titlepart[0]
        elif len(titlepart) == 2:
            # modelname+title or title+date
            if '-' in titlepart[1]:
                # title+date
                cover.title = titlepart[0]
                cover.pub_date = titlepart[1]
            else:
                # modelnamee+title
                cover.model_name = titlepart[0]
                cover.title = titlepart[1]
        elif len(titlepart) == 3:
            cover.model_name = titlepart[0]
            cover.title = titlepart[1]
            cover.pub_date = titlepart[2]

        # print 'name: ' + cover.model_name + ', title: ' + cover.title + ', date: ' + cover.pub_date
        cover.group_path = group_name
        cover.img_count = len(detail_list)

        imgfullpath = os.path.join(root_dir, cover.img_path)
        if os.path.exists(imgfullpath):
            img = Image.open(unicode(imgfullpath, 'utf8'))
            cover.img_width = img.width
            cover.img_height = img.height

        cover_list.append(cover)

    # if is_sort:
    #     cover_list.sort(cover_sort, reverse=True)

    cover_response = CoverResponse()
    cover_response.coverlist = cover_list
    return MyEncoder().encode(cover_response)


def get_details(root_dir, group_path):
    res = []
    detail_path = os.path.join(root_dir, group_path)
    file_list = os.listdir(detail_path)
    for index in range(0, len(file_list)):
        img_path = os.path.join(detail_path, file_list[index])
        if not os.path.exists(img_path):
            continue
        if DS_Store in file_list[index]:
            os.remove(img_path)
            continue

        detail = Detail()
        detail.img_thumbnail_path = os.path.join(group_path, file_list[index])
        detail.img_name = file_list[index]

        imgfullpath = os.path.join(root_dir, detail.img_thumbnail_path)
        if os.path.exists(imgfullpath):
            img = Image.open(imgfullpath)
            detail.img_width = img.width
            detail.img_height = img.height

        res.append(detail)

    res.sort(detail_sort)

    detail_response = DetailResponse()
    detail_response.detaillist = res
    return MyEncoder().encode(detail_response)

# print get_covers()
# print get_details('/Users/yangqiang/Downloads/myFolder/3kez')
