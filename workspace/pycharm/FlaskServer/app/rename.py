# -*- coding: utf-8 -*
import shutil,os

root_dir = '/Users/yangqiang/Downloads/sysc'
group_list = os.listdir(root_dir)
for index in range(0, len(group_list)):
    group_name = group_list[index]
    new_group_name = group_name.replace('---', '')

    shutil.move(os.path.join(root_dir, group_name), os.path.join(root_dir, new_group_name))