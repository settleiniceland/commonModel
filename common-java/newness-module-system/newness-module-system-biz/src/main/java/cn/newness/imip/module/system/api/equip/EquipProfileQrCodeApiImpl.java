package cn.newness.imip.module.system.api.equip;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.newness.imip.module.infra.api.file.FileApi;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author machuran
 * @date 8/30/2024
 * @time 10:12 AM
 * @Description
 */
@Service
public class EquipProfileQrCodeApiImpl implements EquipProfileQrCodeApi {
    @Value("${temporary.directory}")
    private String TEMP_DIR;
    @Resource
    private FileApi fileApi;
    @Override
    public String createAndUploadQrCode(String equipProfileId,String equipName,String equipCode,String text) {
        File qrcodefile = FileUtil.file(TEMP_DIR+"/"+equipProfileId+".png");
        BufferedImage qrImage = QrCodeUtil.generate(equipProfileId, 500, 500);
        int textHeight = 50;  // 文字区域高度
        BufferedImage finalImage = new BufferedImage(qrImage.getWidth(), qrImage.getHeight() + textHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = finalImage.createGraphics();
        // 设置背景为白色
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, qrImage.getWidth(), qrImage.getHeight() + textHeight);
        // 绘制二维码
        graphics.drawImage(qrImage, 0, textHeight, null);
        // 设置字体及颜色
        graphics.setFont(new Font("宋体", Font.BOLD, 23));//宋体黑色size23
        graphics.setColor(Color.BLACK);
        // 绘制文字
        graphics.drawString(text, 7, textHeight - 10);  //文字距离二维码10像素，距左边7像素
        graphics.dispose();
        try {
            ImageIO.write(finalImage,"png",qrcodefile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String qrUrl = "";
        try (FileInputStream fis = new FileInputStream(qrcodefile)) {
            byte[] qrFileBytes = new byte[(int) qrcodefile.length()];
            fis.read(qrFileBytes);
            qrUrl = fileApi.createFile(equipName + equipCode + "二维码文件", equipCode + "/" + UUID.randomUUID().toString().replace("-","") +".png", qrFileBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            qrcodefile.delete();
        }
        return qrUrl;
    }
}
