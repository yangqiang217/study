#include <jni.h>
#include <android/log.h>

#define LOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO,"jason",FORMAT,##__VA_ARGS__);
#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR,"jason",FORMAT,##__VA_ARGS__);

//封装格式
#include "libavformat/avformat.h"
//解码
#include "libavcodec/avcodec.h"
//缩放
#include "libswscale/swscale.h"

JNIEXPORT void JNICALL
Java_com_ffmpeg_1study_MainActivity_video_1decode(JNIEnv *env, jobject instance, jstring input_,
                                                  jstring output_) {
    const char *input_cstr = (*env)->GetStringUTFChars(env, input_, 0);
    const char *output_cstr = (*env)->GetStringUTFChars(env, output_, 0);

//
////1.注册组件
//    av_register_all();
//
////封装格式上下文
//    AVFormatContext *pFormatCtx = avformat_alloc_context();
//
////2.打开输入视频文件
//    if (avformat_open_input(&pFormatCtx, input_cstr, NULL, NULL) != 0) {
//        LOGE("%s", "打开输入视频文件失败");
//        return;
//    }
////3.获取视频信息
//    if (avformat_find_stream_info(pFormatCtx, NULL) < 0) {
//        LOGE("%s", "获取视频信息失败");
//        return;
//    }
//
////视频解码，需要找到视频对应的AVStream所在pFormatCtx->streams的索引位置
//    int video_stream_idx = -1;
//    int i = 0;
//    for (; i < pFormatCtx->nb_streams; i++) {
////根据类型判断，是否是视频流
//        if (pFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO) {
//            video_stream_idx = i;
//            break;
//        }
//    }
//
////4.获取视频解码器
//    AVCodecContext *pCodeCtx = pFormatCtx->streams[video_stream_idx]->codec;
//    AVCodec *pCodec = avcodec_find_decoder(pCodeCtx->codec_id);
//    if (pCodec == NULL) {
//        LOGE("%s", "无法解码");
//        return;
//    }
//
////5.打开解码器
//    if (avcodec_open2(pCodeCtx, pCodec, NULL) < 0) {
//        LOGE("%s", "解码器无法打开");
//        return;
//    }
//
////编码数据
//    AVPacket *packet = (AVPacket *) av_malloc(sizeof(AVPacket));
//
////像素数据（解码数据）
//    AVFrame *frame = av_frame_alloc();
//    AVFrame *yuvFrame = av_frame_alloc();
//
////只有指定了AVFrame的像素格式、画面大小才能真正分配内存
////缓冲区分配内存
//    uint8_t *out_buffer = (uint8_t *) av_malloc(
//            avpicture_get_size(AV_PIX_FMT_YUV420P, pCodeCtx->width, pCodeCtx->height));
////初始化缓冲区
//    avpicture_fill((AVPicture *) yuvFrame, out_buffer, AV_PIX_FMT_YUV420P, pCodeCtx->width, pCodeCtx->height);
//
//
////输出文件
//    FILE *fp_yuv = fopen(output_cstr, "wb");
//
////用于像素格式转换或者缩放
//    struct SwsContext *sws_ctx = sws_getContext(
//            pCodeCtx->width, pCodeCtx->height, pCodeCtx->pix_fmt,
//            pCodeCtx->width, pCodeCtx->height, AV_PIX_FMT_YUV420P,
//            SWS_BILINEAR, NULL, NULL, NULL);
//
//    int len, got_frame, framecount = 0;
////6.一阵一阵读取压缩的视频数据AVPacket
//    while (av_read_frame(pFormatCtx, packet) >= 0) {
////解码AVPacket->AVFrame
//        len = avcodec_decode_video2(pCodeCtx, frame, &got_frame, packet);
//
////Zero if no frame could be decompressed
////非零，正在解码
//        if (got_frame) {
////frame->yuvFrame (YUV420P)
////转为指定的YUV420P像素帧
//            sws_scale(sws_ctx,
//                      frame->data, frame->linesize, 0, frame->height,
//                      yuvFrame->data, yuvFrame->linesize);
//
////向YUV文件保存解码之后的帧数据
////AVFrame->YUV
////一个像素包含一个Y
//            int y_size = pCodeCtx->width * pCodeCtx->height;
//            fwrite(yuvFrame->data[0], 1, y_size, fp_yuv);
//            fwrite(yuvFrame->data[1], 1, y_size / 4, fp_yuv);
//            fwrite(yuvFrame->data[2], 1, y_size / 4, fp_yuv);
//
//            LOGI("解码%d帧", framecount++);
//        }
//
//        av_free_packet(packet);
//    }
//
//    fclose(fp_yuv);
//
//    av_frame_free(&frame);
//    avcodec_close(pCodeCtx);
//    avformat_free_context(pFormatCtx);
//
//    (*env)->ReleaseStringUTFChars(env, input_, input_cstr);
//    (*env)->ReleaseStringUTFChars(env, output_, output_cstr);
}