package cn.newness.imip.framework.excel.core.handler;



import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.List;


/**主管单元格注入内容后调节行高,已经相同值合并事项,空白行添加allBroder样式
 * @author machuran
 * @date 8/27/2024
 * @time 10:34 AM
 * @Description
 */
public class MyCellWriteHandler implements CellWriteHandler {
    private static final Logger log = LoggerFactory.getLogger(MyCellWriteHandler.class);
    List<Integer> mergeColumnNumList;
    List<Integer> allBroderColumnNumList;
    public MyCellWriteHandler(List<Integer> mergeColumnNumList,List<Integer> allBroderColumnNumList) {
        this.mergeColumnNumList=mergeColumnNumList;
        this.allBroderColumnNumList=allBroderColumnNumList;
    }
    @Override//填入当前行数据之后立马执行【该行每个单元格注入后都会执行一次】
    public void afterCellDispose(CellWriteHandlerContext context) {
        Row currentRow = context.getCell().getRow();//获取当前行
        Sheet sheet = currentRow.getSheet();
        if(currentRow.getRowNum()==0){//通常情况下最后一次调用其行号是0，如果要在第0行插入数据，则需要回来改代码
            return;
        }
        /*********************************设置自动行高相关逻辑*********************************/
        float columnWidth = sheet.getColumnWidthInPixels(context.getCell().getColumnIndex());//获取当前单元格的宽度【单位：像素】
        org.apache.poi.ss.usermodel.Font poiFont = sheet.getWorkbook().getFontAt(context.getCell().getCellStyle().getFontIndex());
        TextLayout textLayout = new TextLayout("一",
                new Font(poiFont.getFontName(),
                        Font.PLAIN,
                        poiFont.getFontHeightInPoints()),
                new FontRenderContext(null,true,true));
        float oneWidth = textLayout.getAdvance();//获取单个字符宽度
        float testHeight = textLayout.getAscent() + textLayout.getDescent();//获取每行的高度
        currentRow.setHeightInPoints((
                currentRow.getCell(context.getCell()
                        .getColumnIndex())
                        .getStringCellValue()
                        .length()
                * oneWidth / columnWidth*testHeight)
                +
                (3 * sheet.getDefaultRowHeightInPoints())//计算有原有高度，然后加上两行
        );
        /*********************************合并列相关逻辑*************************************/
        Row previousRow = sheet.getRow(currentRow.getRowNum() - 1);//获取上一行
        /*处理项目列表合并事项*/
        mergeColumnNumList.forEach(mergeColumnNum -> {
            if(mergeColumnNum==context.getCell().getColumnIndex()){//判断是否是要合并的列，是的话才进行下一步合并判断，
                Cell currentRowCell = currentRow.getCell(mergeColumnNum);
                Cell previousRowCell = previousRow.getCell(mergeColumnNum);
                if(currentRowCell !=null && previousRowCell !=null){
                    String currentValue = currentRowCell.getStringCellValue();
                    String previousValue = previousRowCell.getStringCellValue();
                    if(currentValue.equals(previousValue)){//文本内容和上一行相同则直接和上一行合并【只有值相同时我才费劲巴拉给他判断上一行是否是合并行】
                        int startRow = previousRow.getRowNum();
                        //先判断当前行是否是合并行并将startRow和endRow根据情况更新【防止重叠合并错误】
                        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
                        for (int i = 0; i < mergedRegions.size(); i++) {//求得当前表所有的合并单元格对象
                            log.info("合并对象【（开始行，结束行，开始列，结束列）】：-->(<"+mergedRegions.get(i).getFirstRow()+","+mergedRegions.get(i).getLastRow()+">,<"+mergedRegions.get(i).getFirstColumn()+","+mergedRegions.get(i).getLastColumn()+">)");
                            log.info("当前行："+currentRow.getRowNum()+"；上一行对象【（行，列）】：("+startRow+","+context.getCell().getColumnIndex()+")");
                            if (startRow >= mergedRegions.get(i).getFirstRow() //上一行行号大于等于合并单元格开始行号
                                    && startRow <= mergedRegions.get(i).getLastRow()//上一行行号小于等于合并单元格结束行号
                                    && context.getCell().getColumnIndex() >= mergedRegions.get(i).getFirstColumn()//上一行列号大于等于合并单元格开始列号【可有可无，自己的数据毕竟只有列合并的场景】
                                    && context.getCell().getColumnIndex() <= mergedRegions.get(i).getLastColumn()) {//上一行列号小于等于合并单元格开始列号【可有可无，自己的数据毕竟只有列合并的场景】
                                log.info("进去了-合并对象【（开始行，结束行，开始列，结束列）】：-->("+mergedRegions.get(i).getFirstRow()+","+mergedRegions.get(i).getLastRow()+","+mergedRegions.get(i).getFirstColumn()+","+mergedRegions.get(i).getLastColumn()+")");
                                // 当前行在合并区域内
                                startRow = mergedRegions.get(i).getFirstRow();
                                sheet.removeMergedRegion(i);//删除之前的合并单元格中，原单元格的值会保存在原合并单元格的右上角子单元格里面，所以这里可以直接删了就行
                                break; // 找到一个合并区域即可
                            }
                        }
                        //判断完成之后再进行真正的合并
                        log.info("此次合并行是：("+startRow+","+currentRow.getRowNum()+")");
                        CellRangeAddress cellAddresses = new CellRangeAddress(startRow, currentRow.getRowNum(), mergeColumnNum, mergeColumnNum);
                        sheet.addMergedRegion(cellAddresses);
                    }
                }
            }
        });
        /*********************************某些无内容单元格设置All Border格式逻辑*************************************/
        allBroderColumnNumList.forEach(allBroderColumn ->{
            Cell allBroderCell = currentRow.getCell(allBroderColumn);
            if(allBroderCell!=null){
                return;
            }
            allBroderCell = currentRow.createCell(allBroderColumn);
            CellStyle cellStyle = allBroderCell.getSheet().getWorkbook().createCellStyle();
            // 设置单元格的边框为 All Border
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            allBroderCell.setCellStyle(cellStyle);
        });
    }
}
