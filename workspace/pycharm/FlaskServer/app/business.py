# -*- coding: utf-8 -*-
import os

from flask import request, make_response, session

from app.MyEncoder import MyEncoder
from app.basemodel.Tab import Tab
from app.const import ROOT_DIR_XUEMEI, ROOT_DIR_XUEMEI_THUMBNAIL, ROOT_DIR_NAINAI, ROOT_DIR_NAINAI2, \
    THUMBNAIL_DIR_NAME, IMG_WIDTH_NONE, DEFAULT_PAGE_SIZE
from app.utils import file_util_imggroup
from app.utils import file_util_mix
from app.responsemodel.ResponseModel import TabResponse


def config():
    return "success"


def get_tabs():
    tab_list = []

    tab0 = Tab()
    tab0.tab_index = 0
    tab0.tab_name = '学妹'
    tab_list.append(tab0)

    tab1 = Tab()
    tab1.tab_index = 1
    tab1.tab_name = '奈奈'
    tab_list.append(tab1)

    # tab2 = Tab()
    # tab2.tab_index = 2
    # tab2.tab_name = 'tabname2'
    # tab_list.append(tab2)

    tab_response = TabResponse()
    tab_response.tablist = tab_list
    return MyEncoder().encode(tab_response)


def get_covers(tab_index, img_width):
    page = 0
    if request.args.has_key("page"):
        page = int(request.args.get("page"))

    page_size = request.args.get("page_size")
    # 分页的时候搜索出所有的
    model_name = request.args.get("model_name")
    if page_size is None:
        page_size = DEFAULT_PAGE_SIZE

    if tab_index == 0:
        return file_util_imggroup.get_covers(ROOT_DIR_XUEMEI_THUMBNAIL, img_width, page, page_size, model_name, True)
    if tab_index == 1:
        return file_util_imggroup.get_covers(ROOT_DIR_NAINAI, False)
    if tab_index == 2:
        return file_util_mix.get_covers(ROOT_DIR_XUEMEI, True)


def get_details(tab_index, img_width, group_name):
    if tab_index == 0:
        return file_util_imggroup.get_details(ROOT_DIR_XUEMEI_THUMBNAIL, img_width, group_name)
    if tab_index == 1:
        return file_util_imggroup.get_details(ROOT_DIR_NAINAI, group_name)
    if tab_index == 2:
        return file_util_mix.get_details(ROOT_DIR_XUEMEI, group_name)


def get_root_dir_by_tab_index(tab_index):
    if tab_index == 0:
        return ROOT_DIR_XUEMEI
    if tab_index == 1:
        return ROOT_DIR_NAINAI
    if tab_index == 2:
        return ROOT_DIR_NAINAI2


# img_width: 全图取0
def get_single_photo(tab_index, img_width, group_path, file_name):
    # 全图
    file_dir = os.path.join(get_root_dir_by_tab_index(tab_index), group_path, file_name)
    if img_width != IMG_WIDTH_NONE:
        file_dir = os.path.join(get_root_dir_by_tab_index(tab_index), THUMBNAIL_DIR_NAME, str(img_width), group_path, file_name)
    if request.method == 'GET':
        if group_path is None or file_name is None:
            pass
        else:
            image_data = open(file_dir, "rb").read()
            response = make_response(image_data)
            response.headers['Content-Type'] = 'image/png'
            return response
    else:
        return ''