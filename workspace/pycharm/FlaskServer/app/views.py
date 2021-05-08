from app import app
from flask import request
import business


# config
@app.route('/config', methods=['GET'])
def config():
    return business.config()

# tab list
@app.route('/tablist', methods=['GET'])
def get_tab_list():
    return business.get_tabs()


# coverlist
@app.route('/coverlist/<int:tab_index>/<int:img_width>', methods=['GET'])
def get_cover_list(tab_index, img_width):
    return business.get_covers(tab_index, img_width)


@app.route('/detaillist/<int:tab_index>/<int:img_width>/<string:group_name>', methods=['GET'])
def get_detail_list(tab_index, img_width, group_name):
    return business.get_details(tab_index, img_width, group_name)


# image list
@app.route('/image/<int:tab_index>/<int:img_width>/<string:group_path>/<string:file_name>', methods=['GET'])
def show_photo(tab_index, img_width, group_path, file_name):
    return business.get_single_photo(tab_index, img_width, group_path, file_name)


@app.route('/posttest', methods=['POST'])
def post_test():
    name=request.form['name']
    psd=request.form['psd']
    print name
    print psd
    return 'ok shit'