# -*- coding: utf-8 -*-
import os
import sys

from PIL import Image

from app.MyEncoder import MyEncoder
from app.basemodel.Cover import Cover
from app.basemodel.Detail import Detail
from app.basemodel.Detail import detail_sort
from app.basemodel.Detail import detail_sort_str
from app.responsemodel.ResponseModel import CoverResponse
from app.responsemodel.ResponseModel import DetailResponse
from app.const import DS_Store, THUMBNAIL_COUNT_FLAG, IMG_WIDTH_NONE
import cStringIO

reload(sys)
sys.setdefaultencoding('utf8')


# root_dir: /.sysc/.thumbnail
def get_covers(thumbnail_root, img_width, page, page_size, model_name, is_sort=False):
    cover_list = []
    thumbnail_dir = os.path.join(thumbnail_root, str(img_width))
    if not os.path.exists(thumbnail_dir):
        return "thumbnail_dir " + thumbnail_dir + " doesn't exist"

    for_search = False
    if model_name is not None and len(model_name) != 0 and len(model_name.strip()) != 0:
        for_search = True
        model_name = model_name.strip()

    group_list = os.listdir(thumbnail_dir)
    if is_sort:
        group_list.sort(_group_sort, reverse=True)

    for index in range(page * page_size, len(group_list)):
        # if len(cover_list) == 20:
        #     break

        group_name = group_list[index]

        group_path = os.path.join(thumbnail_dir, group_name)
        if not os.path.isdir(group_path):
            continue

        if for_search and model_name not in group_name:
            continue

        cover = Cover()

        img_list = os.listdir(group_path)
        if len(img_list) == 0:
            print(group_name + " is empty")
            continue
        if DS_Store in img_list:
            os.remove(os.path.join(group_path, DS_Store))
            img_list.remove(DS_Store)
        if ".jpg" in img_list:
            os.remove(os.path.join(group_path, ".jpg"))
            img_list.remove(".jpg")

        count = 0
        for i in range(0, len(img_list)):
            if THUMBNAIL_COUNT_FLAG in img_list[i]:
                count = img_list[i].split(".")[0]
                img_list.remove(img_list[i])
                break

        img_list.sort(detail_sort_str)
        cover.img_path = os.path.join(str(img_width), group_name, img_list[0])

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
        cover.img_count = count

        imgfullpath = os.path.join(thumbnail_root, cover.img_path)
        try:
            if os.path.exists(imgfullpath):
                img = Image.open(unicode(imgfullpath, 'utf8'))
                cover.img_width = img.width
                cover.img_height = img.height
        except Exception:
            print ("cover open img error")

        cover_list.append(cover)

        if not for_search and len(cover_list) == page_size:
            break

    cover_response = CoverResponse()
    cover_response.coverlist = cover_list
    return MyEncoder().encode(cover_response)


def _get_title(titlepart):
    if len(titlepart) == 0:
        return ""
    elif len(titlepart) == 1:
        return titlepart[0]
    elif len(titlepart) == 2:
        # modelname+title or title+date
        if '-' in titlepart[1]:
            # title+date
            return titlepart[0]
        else:
            # modelnamee+title
            return titlepart[1]
    elif len(titlepart) == 3:
        return titlepart[1]


def _get_model_name(titlepart):
    if len(titlepart) == 0 or len(titlepart) == 1:
        return ""
    elif len(titlepart) == 2:
        # modelname+title or title+date
        if '-' in titlepart[1]:
            # title+date
            return ""
        else:
            # modelnamee+title
            return titlepart[0]
    elif len(titlepart) == 3:
        return titlepart[0]


def _get_pub_date(titlepart):
    if len(titlepart) == 0 or len(titlepart) == 1:
        return ""
    elif len(titlepart) == 2:
        # modelname+title or title+date
        if '-' in titlepart[1]:
            # title+date
            return titlepart[1]
        else:
            # modelnamee+title
            return ""
    elif len(titlepart) == 3:
        return titlepart[2]


def _group_sort(group_name1, group_name2):
    pub_date1 = _get_pub_date(group_name1.split('_'))
    pub_date2 = _get_pub_date(group_name2.split('_'))

    if len(pub_date1) == 0 and len(pub_date2) != 0:
        return -1
    if len(pub_date1) != 0 and len(pub_date2) == 0:
        return 1
    if len(pub_date1) == 0 and len(pub_date2) == 0:
        return 0

    templist1 = pub_date1.split('-')
    templist2 = pub_date2.split('-')
    # print str(templist1) + ', ' + str(templist2)
    for i in range(0, len(templist1)):
        try:
            var1 = int(templist1[i])
        except:
            print("shit")
        var2 = int(templist2[i])
        if var1 > var2:
            return 1
        if var1 < var2:
            return -1
    return 0


def get_details(thumbnail_root, img_width, group_name):
    res = []
    detail_path = os.path.join(thumbnail_root, str(img_width), group_name)
    if not os.path.exists(detail_path):
        return "detail_path " + detail_path + " doesn't exist"

    file_list = os.listdir(detail_path)
    for index in range(0, len(file_list)):
        img_path = os.path.join(detail_path, file_list[index])
        if not os.path.exists(img_path):
            continue
        if DS_Store in file_list[index]:
            os.remove(img_path)
            continue

        detail = Detail()
        detail.img_thumbnail_path = os.path.join(str(img_width), group_name, file_list[index])
        detail.img_ori_path = os.path.join(str(IMG_WIDTH_NONE), group_name, file_list[index])
        detail.img_name = file_list[index]

        imgfullpath = os.path.join(thumbnail_root, detail.img_thumbnail_path)
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
