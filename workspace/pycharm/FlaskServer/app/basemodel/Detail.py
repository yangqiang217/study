import re


class Detail:
    def __init__(self):
        pass

    # for sort
    img_name = ''
    img_thumbnail_path = ''
    img_ori_path = ''
    img_width = 0.0
    img_height = 0.0


def detail_sort(detail1, detail2):
    name1 = detail1.img_name
    name2 = detail2.img_name
    return detail_sort_str(name1, name2)


def detail_sort_str(name1, name2):
    try:
        if len(name1) == 0 and len(name2) != 0:
            return -1
        if len(name1) != 0 and len(name2) == 0:
            return 1
        if len(name1) == 0 and len(name2) == 0:
            return 0

        name1_without_format = name1.split('.')[0]
        name2_without_format = name2.split('.')[0]

        num_rule = re.compile('^[0-9]*$')

        if num_rule.match(name1_without_format) and num_rule.match(name2_without_format):
            num1 = int(name1_without_format)
            num2 = int(name2_without_format)
            if num1 > num2:
                return 1
            elif num1 < num2:
                return -1

        if len(name1_without_format) < len(name2_without_format):
            shorter_len = len(name1_without_format)
        else:
            shorter_len = len(name2_without_format)
        for i in range(0, shorter_len):
            at_i1 = name1_without_format[i]
            at_i2 = name2_without_format[i]

            if num_rule.match(at_i1):
                at_i_num1 = int(at_i1)
            else:
                at_i_num1 = ord(at_i1)

            if num_rule.match(at_i2):
                at_i_num2 = int(at_i2)
            else:
                at_i_num2 = ord(at_i2)

            if at_i_num1 > at_i_num2:
                return 1
            elif at_i_num1 < at_i_num2:
                return -1
    except:
        print("except")

    return 0
    # num_rule = re.compile('^[0-9]*$')
    # idx = 0
    # for i in range(len(name1_without_format) - 1, -1, -1):
    #     is_number = num_rule.match(name1_without_format[i])
    #     if not is_number:
    #         idx = i + 1
    #         break
    # nums1_str = name1_without_format[idx:]
    #
    # idx = 0
    # for i in range(len(name2_without_format) - 1, -1, -1):
    #     is_number = num_rule.match(name2_without_format[i])
    #     if not is_number:
    #         idx = i + 1
    #         break
    # nums2_str = name2_without_format[idx:]
    #
    # try:
    #     num1 = int(nums1_str)
    #     num2 = int(nums2_str)
    #     if num1 > num2:
    #         return 1
    #     if num1 < num2:
    #         return -1
    # except ValueError:
    #     print 'value error'
    #     return 1

