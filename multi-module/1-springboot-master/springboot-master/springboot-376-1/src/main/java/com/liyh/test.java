package com.liyh;

public class test {
    public static void main(String[] args) {
        // 1.集中器登录主站注册帧报文
        // 该帧为集中器发送给主站的登录注册帧
        // 68 32 00 32 00 68   // 帧头，包含帧起始标志和长度L.
        // C9                  // 1100'1001. 控制域C。
        //                     // D7=1, （终端发送）上行方向。
        //                     // D6=1, 此帧来自启动站。
        //                     // D5=0, (上行方向)要求访问位。表示终端无事件数据等待访问。
        //                     // D4=0, 保留
        //                     // D3~D0=9, 功能码。链路测试
        // 03 44               // 行政区划码
        // 04 00               // 终端地址
        // 00                  // 主站地址和组地址标志。终端为单地址。
        //                     // 终端启动的发送帧的 MSA 应为 0, 其主站响应帧的 MSA 也应为 0.
        // 02                  // 应用层功能码。AFN=2, 链路接口测试
        // 71                  // 0111'0001. 帧序列域。无时间标签、单帧、需要确认。
        // 00 00               // 信息点。DA1和DA2全为“0”时，表示终端信息点。
        // 01 00               // 信息类。F9, 终端事件记录配置设置。
        // 88 16               // 帧尾，包含用户区数据校验和和帧结束标志
        String packet = "68 32 00 32 00 68 C9 03 44 04 00 00 02 71 00 00 01 00 88 16";

        String[] newPackage = packet.split(" ");

        // 68 帧起始符
        if (!"68".equals(newPackage[0])) {
            return;
        }

        // 32 00 数据长度2字节。链路层传输顺序为低位在前，高位在后；低字节在前，高字节在后。
        // 首先前后颠倒得到：00 32 （这里为16进制）
        // 将其转换为2进制：00000000 00110010
        String first = "";
        String second = "";
        if ("00".equals(newPackage[2])) {
            first = "00000000";
        } else {
            first = hexadecimalToBinary(newPackage[2]);
        }
        if ("00".equals(newPackage[1])) {
            second = "00000000";
        } else {
            second = hexadecimalToBinary(newPackage[1]);
        }
        String agreement = first.concat(second);
        // 最低d1～d0两位为协议类型，为10代表为376.1协议使用标志
        if (!"10".equals(agreement.substring(14))) {
            return;
        }

        // d15～d2为长度，为报文的用户数据区长度，对应的二进制值为1100，转换为十进制为12，即原始报文的红色部分
        int length = Integer.valueOf(agreement.substring(0, 14), 2);

        if (length != packet.substring(18, 53).split(" ").length) {
            return;
        }

        // 32 00 内容解释同上，使用2个长度表示长度确认比对
        if ("00".equals(newPackage[4])) {
            first = "00000000";
        } else {
            first = hexadecimalToBinary(newPackage[4]);
        }
        if ("00".equals(newPackage[3])) {
            second = "00000000";
        } else {
            second = hexadecimalToBinary(newPackage[3]);
        }

        if (!agreement.equals(first.concat(second))) {
            return;
        }

        // 68 帧起始符
        if (!"68".equals(newPackage[5])) {
            return;
        }

        // C9 控制域C表示报文传输方向和所提供的传输服务类型的信息，二进制数值为 1100 1001，对应d7～d0位
        String control = hexadecimalToBinary(newPackage[6]);
        // DIR = D7 = 1, DIR=0，表示此帧报文是由主站发出的下行报文； DIR=1，表示此帧报文是由终端发出的上行报文
        // PRM = D6 = 1, PRM=0，表示此帧报文来自从动站；PRM=1，表示此帧报文来自启动站
        // DIR=1所以为上行，,故ACD=D5=0，ACD位用于上行响应报文中，ACD=1表示终端有重要事件等待访问；ACD=0表示终端无事件数据等待访问。
        // ACD置“1”和置“0”规则：
        // 1. 自上次收到报文后发生新的重要事件，ACD位置“1”；
        // 2. 收到主站请求事件报文并执行后，ACD位置“0”。
        String dir = control.substring(0, 1);
        String dirName = "";
        if ("0".equals(dir)) {
            dirName = "终端发出的上行报文";
        } else {
            dirName = "主站发出的下行报文";
        }
        String prm = control.substring(1, 2);
        String prmName = "";
        if ("0".equals(prm)) {
            prmName = "报文来自从动站";
        } else {
            prmName = "报文来自启动站";
        }

        // DIR=1为上行, D4=0， 保留；DIR=0为下行, D4=1，帧计数有效位FCV

        // D3～D0：功能码 1001 二进制转换为十进制为: 9
        // 当启动标志位 PRM=1 时，功能码定义：0 — 备用，1 发送∕确认 复位命令，2～3 — 备用，4 发送∕无回答 用户数据，5～8 — 备用
        // 9 请求∕响应帧 链路测试，10 请求∕响应帧 请求1级数据，11 请求∕响应帧 请求2级数据，12～15 — 备用
        // 当启动标志位 PRM=0 时，功能码定义：0 确认 认可，1～7 — 备用，8 响应帧 用户数据，9 响应帧 否认：无所召唤的数据，10 — 备用
        // 11 响应帧 链路状态，12～15 — 备用
        String functionCode = "";
        try {
            functionCode = Integer.valueOf(control.substring(4), 2).toString();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        };
        String functionName = "";
        if ("1".equals(prm)) {
            if ("0".equals(functionCode)) {
                functionName = "备用";
            } else if ("1".equals(functionCode)) {
                functionName = "复位命令";
            } else if ("2".equals(functionCode) || "3".equals(functionCode)) {
                functionName = "备用";
            } else if ("4".equals(functionCode)) {
                functionName = "用户数据";
            } else if ("5".equals(functionCode) || "6".equals(functionCode) || "7".equals(functionCode) || "8".equals(functionCode)) {
                functionName = "备用";
            } else if ("9".equals(functionCode)) {
                functionName = "链路测试";
            } else if ("10".equals(functionCode)) {
                functionName = "请求1级数据";
            } else if ("11".equals(functionCode)) {
                functionName = "请求2级数据";
            } else if ("12".equals(functionCode) || "13".equals(functionCode) || "14".equals(functionCode) || "15".equals(functionCode)) {
                functionName = "备用";
            }
        } else {
            if ("0".equals(functionCode)) {
                functionName = "认可";
            } else if ("1".equals(functionCode) || "2".equals(functionCode) || "3".equals(functionCode) || "4".equals(functionCode) || "5".equals(functionCode) || "6".equals(functionCode) || "7".equals(functionCode)) {
                functionName = "备用";
            } else if ("8".equals(functionCode)) {
                functionName = "用户数据";
            } else if ("9".equals(functionCode)) {
                functionName = "否认：无所召唤的数据";
            } else if ("10".equals(functionCode)) {
                functionName = "备用";
            } else if ("11".equals(functionCode)) {
                functionName = "链路状态";
            } else if ("12".equals(functionCode) || "13".equals(functionCode) || "14".equals(functionCode) || "15".equals(functionCode)) {
                functionName = "备用";
            }
        }

        // 地址域 A
        // 03 44 04 00 00 地址域：地址域分为三部分，03 44|04 00|00
        // 地址域由行政区划码A1、终端地址A2、主站地址和组地址标志A3组成
        //     地址域                   数据格式      字节数     对应数值
        // 行政区划码A1                   BCD          2          03 44
        // 终端地址A2                     BIN          2          04 00
        // 主站地址和组地址标志A3         BIN          1           00
        // 第一部分为行政区划码A1,行政区划码按GB 2260—的规定执行，数据格式为两字节BCD码-03 44，字节顺序为低位在前高位在后，实际数值应为4403
        String regionCode = newPackage[8].concat(newPackage[7]);

        // 第二部分为终端地址A2，终端地址A2选址范围为1～65535。A2=0000H为无效地址，A2=FFFFH且A3的D0位为“1”时表示系统广播地址，
        // 数据格式为两字节BIN码--04 00，字节顺序为低位在前高位在后，实际数值应为0004，然后16进制转换为十进制
        String terminalAddress = Integer.valueOf(newPackage[10].concat(newPackage[9]), 16).toString();

        // 第三部分为主站地址和组地址标志A3，A3的D0位为终端组地址标志，D0=0表示终端地址A2为单地址；D0=1表示终端地址A2为组地址；
        String stationAddress = newPackage[11];

        if ("0000H".equals(terminalAddress)) {
            terminalAddress = "无效地址";
        } else if ("FFFFH".equals(terminalAddress) && ("1".equals(stationAddress.substring(1)))) {
            terminalAddress = "系统广播地址";
        }

        // 第三部分为主站地址和组地址标志A3，A3的D0位为终端组地址标志，D0=0表示终端地址A2为单地址；D0=1表示终端地址A2为组地址；
        // A3的D1～D7组成0～127个主站地址MSA。
        // 主站启动的发送帧的MSA应为非零值，其终端响应帧的MSA应与主站发送帧的MSA相同。
        // 终端启动发送帧的MSA应为零，其主站响应帧的MSA也应为零。

        // 链路用户数据
        // 02   应用层功能码AFN：应用层功能码由一字节组成，采用二进制编码表示，对应数值为02H，代表应用功能为链路接口检测
        // 应用层功能码AFN - 帧序列域SEQ - 数据单元标识1 - 数据单元1 -...- 数据单元标识n - 数据单元n - 附加信息域AUX
        // 对于应用层需要加密的关键数据，加密的数据区包括：应用层功能码、帧序列域、数据单元标识及数据单元部分。
        // 00H 确认∕否认，01H 复位，02H 链路接口检测，03H 中继站命令，04H 设置参数，05H 控制命令，06H 身份认证及密钥协商
        // 07H 备用，08H 请求被级联终端主动上报，09H 请求终端配置，0AH 查询参数，0BH 请求任务数据，0CH 请求1类数据（实时数据）
        // 0DH 请求2类数据（历史数据），0EH 请求3类数据（事件数据），0FH 文件传输，10H 数据转发，11H～FFH 备用
        String applicationCode = newPackage[12];
        String applicationName = "";
        if ("00".equals(applicationCode)) {
            applicationName = "确认∕否认";
        } else if ("01".equals(applicationCode)) {
            applicationName = "复位";
        } else if ("02".equals(applicationCode)) {
            applicationName = "链路接口检测";
        } else if ("03".equals(applicationCode)) {
            applicationName = "中继站命令";
        } else if ("04".equals(applicationCode)) {
            applicationName = "设置参数";
        } else if ("05".equals(applicationCode)) {
            applicationName = "控制命令";
        } else if ("06".equals(applicationCode)) {
            applicationName = "身份认证及密钥协商";
        } else if ("07".equals(applicationCode)) {
            applicationName = "备用";
        } else if ("08".equals(applicationCode)) {
            applicationName = "请求被级联终端主动上报";
        } else if ("09".equals(applicationCode)) {
            applicationName = "请求终端配置";
        } else if ("0A".equals(applicationCode)) {
            applicationName = "查询参数";
        } else if ("0B".equals(applicationCode)) {
            applicationName = "请求任务数据";
        } else if ("0C".equals(applicationCode)) {
            applicationName = "请求1类数据（实时数据）";
        } else if ("0D".equals(applicationCode)) {
            applicationName = "请求2类数据（历史数据）";
        } else if ("0E".equals(applicationCode)) {
            applicationName = "请求3类数据（事件数据）";
        } else if ("0F".equals(applicationCode)) {
            applicationName = "文件传输";
        } else if ("10".equals(applicationCode)) {
            applicationName = "数据转发";
        } else {
            applicationName = "备用";
        }

        // 查询02H的报文格式，链路接口检测命令上行报文格式
        // 68H - L - L - 68H - C - A - AFN=02H - SEQ - 数据单元标识（DA=0） - 数据单元 - CS - 16H
        // 71 帧序列域SEQ长度为1字节，用于描述帧之间的传输序列的变化规则，由于受报文长度限制，数据无法在一帧内传输，需要分成多帧传输（每帧都应有数据单元标识，都可以作为独立的报文处理）
        // 71转换为二进制为 01110001，对应下图的相关标志为：
        // D7    D6   D5   D4    D3-D0
        // TpV  FIR	 FIN  CON  PSEQ∕RSEQ
        // 0    1     1   1     0001
        // TpV：帧时间标签有效位，TpV=0，表示在附加信息域中无时间标签Tp；TpV=1，表示在附加信息域中带有时间标签Tp
        String seq = hexadecimalToBinary(newPackage[13]);
        String tpv = seq.substring(0, 1);
        String tpvName = "";
        if ("0".equals(tpv)) {
            tpvName = "附加信息域中无时间标签Tp";
        } else if ("1".equals(tpv)) {
            tpvName = "附加信息域中带有时间标签Tp";
        }
        // 首帧标志FIR、末帧标志FIN
        // FIR：置“1”，报文的第一帧。
        // FIN：置“1”，报文的最后一帧。
        // FIR、FIN组合状态所表示的含义见下表。
        // FIR	FIN	 应用说明
        // 0	0	多帧：中间帧
        // 0	1	多帧：结束帧
        // 1	0	多帧：第1帧，有后续帧
        // 1	1	单帧
        String fir = seq.substring(1, 2);
        String fin = seq.substring(2, 3);
        String fiName = "";
        if ("0".equals(fir) && "0".equals(fin)) {
            fiName = "多帧：中间帧";
        } else if ("0".equals(fir) && "1".equals(fin)) {
            fiName = "多帧：结束帧";
        } else if ("1".equals(fir) && "0".equals(fin)) {
            fiName = "多帧：第1帧，有后续帧";
        } else if ("1".equals(fir) && "1".equals(fin)) {
            fiName = "单帧";
        }
        // 请求确认标志位CON：在所收到的报文中，CON位置“1”，表示需要对该帧报文进行确认；置“0”，表示不需要对该帧报文进行确认。
        String con = seq.substring(3, 4);
        String conName = "";
        if ("0".equals(con)) {
            conName = "不需要对该帧报文进行确认";
        } else if ("1".equals(con)) {
            conName = "需要对该帧报文进行确认";
        }

        // 启动帧序号PSEQ，取自1字节的启动帧计数器PFC的低4位计数值0～15。
        // 启动帧帧序号计数器PFC，每一对启动站和从动站之间均有1个独立的、由1字节构成的计数范围为0～255的启动帧帧序号计数器PFC，用于记录当前启动帧的序号。
        // 启动站每发送1帧报文，该计数器加1，从0～255循环加1递增；重发帧则不加1。目前该值为0001
        // 响应帧序号RSEQ以启动报文中的PSEQ作为第一个响应帧序号，后续响应帧序号在RSEQ的基础上循环加1递增，数值范围为0～15。
        // 帧序号改变规则
        //
        // 1.启动站发送报文后，当一个期待的响应在超时规定的时间内没有被收到，如果允许启动站重发，则该重发的启动帧序号PSEQ不变。
        // 重发次数可设置，最多3次；重发次数为0，则不允许重发。
        //
        // 2.当TpV=0时，如果从动站连续收到两个具有相同启动帧序号PSEQ的启动报文，通常意味着报文的响应未被对方站收到。
        // 在这种情况下，则重发响应（不必重新处理该报文）。
        //
        // 3.当TpV=0时，如果启动站连续收到两个具有相同响应帧序号RSEQ的响应帧，则不处理第二个响应。
        //
        // 4.终端在开始响应第二个请求前，必须将前一个请求处理结束，终端能同时处理多个请求。
        String pseq = seq.substring(4);

        // 数据单元标识：00 00 01 00
        // 数据单元标识由信息点标识DA和信息类标识DT组成，表示信息点和信息类型
        // 00 00    信息点DA：信息点DA由信息点元DA1和信息点组DA2两个字节构成。DA1：00，DA2：00（字节顺序为低位在前高位在后，02 01实际数值应为0102）
        // DA2采用二进制编码方式表示信息点组，低位在前高位在后，DA1对位表示某一信息点组的1～8个信息点，以此共同构成信息点标识pn（n=1～2040）
        // 当DA1和DA2全为“0”时，表示终端信息点，用p0表示；当DA1=FFH、DA2=00H时，表示所有有效测量点（不含p0）。
        // 信息点标识pn对应于不同信息类标识Fn可以是测量点号、总加组号、控制轮次、直流模拟量端口号、任务号。
        // 运算规则为    pn = (DA2-1) * 8 + DA1     对应位的值就是信息点标识pn，格式见下图。
        String da1 = newPackage[14];
        String da2 = newPackage[15];
        int pn = 0;
        if (!"00".equals(da1) || !"00".equals(da2)) {

        }

        // 01 00    信息类DT由信息类元DT1和信息类组DT2两个字节构成。
        // DT2采用二进制编码方式表示信息类组，DT1对位表示某一信息类组的1～8种信息类型，以此共同构成信息类标识Fn（n=1～248）
        // 运算规则为    Fn = DT2 * 8 + DT1   对应位的值(8421码)就是信息类标识Fn
        // 16进制	真实数值
        //  00        0
        //  01        1
        //  02        2
        //  04        3
        //  08        4
        //  10        5
        //  20        6
        //  40        7
        //  80        8
        // DT1=01 DT2=00 根据公式得出fn=1
        String dt1 = newPackage[16];
        String dt2 = newPackage[17];
        if ("00".equals(dt1)) {
            dt1 = "0";
        } else if ("01".equals(dt1)) {
            dt1 = "1";
        } else if ("02".equals(dt1)) {
            dt1 = "2";
        } else if ("04".equals(dt1)) {
            dt1 = "3";
        } else if ("08".equals(dt1)) {
            dt1 = "4";
        } else if ("10".equals(dt1)) {
            dt1 = "5";
        } else if ("20".equals(dt1)) {
            dt1 = "6";
        } else if ("40".equals(dt1)) {
            dt1 = "7";
        } else if ("80".equals(dt1)) {
            dt1 = "8";
        }
        if ("00".equals(dt2)) {
            dt2 = "0";
        } else if ("01".equals(dt2)) {
            dt2 = "1";
        } else if ("02".equals(dt2)) {
            dt2 = "2";
        } else if ("04".equals(dt2)) {
            dt2 = "3";
        } else if ("08".equals(dt2)) {
            dt2 = "4";
        } else if ("10".equals(dt2)) {
            dt2 = "5";
        } else if ("20".equals(dt2)) {
            dt2 = "6";
        } else if ("40".equals(dt2)) {
            dt2 = "7";
        } else if ("80".equals(dt2)) {
            dt2 = "8";
        }
        int fn = Integer.valueOf(dt2) * 8 + Integer.valueOf(dt1);

        // 02H 链路接口检测对应的pn fn为
        // Fn       名称及说明                                                                         pn
        // F1       登录(全部确认：对收到的报文中的全部数据单元标识进行确认)                           p0
        // F2       退出登录(全部否认：对收到的报文中的全部数据单元标识进行否认)                       p0
        // F3       心跳(按数据单元表示确认和否认：对收到的报文中的全部数据单元标识逐个进行确认/否认)  p0
        // F4～F248 备用
        String command = "";
        if (fn == 1) {
            command = "登录";
        } else if (fn == 2) {
            command = "退出登录";
        } else if (fn == 3) {
            command = "心跳";
        } else {
            command = "备用";
        }

        // 88   帧校验和（CS）是用户数据区的8位位组的算术和，不考虑进位位。
        // 用户数据区包括控制域、地址域、链路用户数据（应用层）三部分。
        // 控制域：C9
        //
        // 地址域：03 44 04 00 00
        //
        // 链路用户数据: 02 71 00 00 01 00
        //
        // 相加之和为:188（16进制）
        //
        // CS为 最后两位 ：88
        String sum = makeChecksum(packet.substring(18, 53));
        if (!newPackage[18].equals(sum.substring(sum.length() - 2))) {
            return;
        }

        // 16   帧结束符
        if (!"16".equals(newPackage[19])) {
            return;
        }

        // 该帧为主站给集中器的确认帧：68 32 00 32 00 68 0B 03 44 04 00 00 00 61 00 00 01 00 B8 16
        StringBuilder result = new StringBuilder();
        result.append("68").append(" ").append(newPackage[1]).append(" ").append(newPackage[2]).append(" ").append(newPackage[3]).append(" ").append(newPackage[4]).append(" 68 ");

        // 控制域C，二进制数值为00001011，对应d7～d0位
        //                  D7               D6              D5              D4             D3～D0
        // 下行方向     传输方向位DIR    启动标志位PRM   帧计数位FCB   帧计数有效位FCV     功能码
        //                  0               0               0               0              1011

        // d7：传输方向位DIR，d7位的数值为0，代表此帧报文是由主站发出的下行报文

        // d6：启动标志位PRM，d6位的数值为0，代表此帧报文来自从动站

        // d5：帧计数位FCB,d5位的数值为0，由于帧计数有效位的值为0，帧计数位无效

        // d4：帧计数有效位FCV，d4位的数值为0，代表帧计数位FCB位无效

        // d3～d0：功能码，对应值为11，当启动标志位PRM=0时，代表该帧报文的帧类型为请求/响应帧，服务功能为链路测试，及使用于AFN=02的应用层功能码
        result.append(binaryToHex("00001011"));

        result.append(" ".concat(newPackage[7]).concat(" ").concat(newPackage[8]).concat(" ").concat(newPackage[9]).concat(" ").concat(newPackage[10].concat(" ").concat(newPackage[11])));

        // 应用层功能码AFN：应用层功能码由一字节组成，采用二进制编码表示，对应数值为00H，确认∕否认报文是对接收报文中需要被确认（CON=1）的回答，
        // 以及终端对所请求的数据不具备响应条件的否认回答。该报文为单帧报文，帧序列域的标志位FIR=1，FIN=1，CON=0。

        result.append(" 00 ");

        // 帧序列域SEQ:帧序列域长度为1字节，61转换为二进制为01100001，对应下图的相关标志为：
        // D7    D6   D5   D4    D3-D0
        // TpV  FIR	 FIN  CON  PSEQ∕RSEQ
        // 0    1     1   0     0001
        // TpV：帧时间标签有效位，TpV=0，表示在附加信息域中无时间标签Tp；TpV=1，表示在附加信息域中带有时间标签Tp

        // 首帧标志FIR、末帧标志FIN
        // FIR：置“1”，报文的第一帧。
        // FIN：置“1”，报文的最后一帧。
        // FIR、FIN组合状态所表示的含义见下表。
        // FIR	FIN	 应用说明
        // 0	0	多帧：中间帧
        // 0	1	多帧：结束帧
        // 1	0	多帧：第1帧，有后续帧
        // 1	1	单帧

        // 请求确认标志位CON：在所收到的报文中，CON位置“1”，表示需要对该帧报文进行确认；置“0”，表示不需要对该帧报文进行确认。
        result.append(binaryToHex("0110".concat(pseq)));

        // 数据单元标识中的pn：信息点DA由信息点元DA1和信息点组DA2两个字节构成。当DA1和DA2全为“0”时，表示终端信息点，用p0表示；
        result.append(" 00 00");

        // 数据单元标识中的Fn：信息类DT由信息类元DT1和信息类组DT2两个字节构成。转换成正序之后为0001，表示F1—全部确认，该确认没有数据体。
        result.append(" 01 00 ");

        // B8 帧校验和
        if (makeChecksum(result.substring(18, 53)).length() > 2) {
            result.append(makeChecksum(result.substring(18, 53)).substring(makeChecksum(result.substring(18, 53)).length() - 2));
        } else {
            result.append(makeChecksum(result.substring(18, 53)));
        }

        // 16 帧结束符
        result.append(" 16");

        System.out.println(result);

    }

    /**
     * 16进制转2进制
     *
     * @param code
     * @return
     */
    public static String hexadecimalToBinary(String code) {
        String ss = "0123456789ABCDEFabcdef";
        char[] ch = code.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (!ss.contains(String.valueOf(ch[i]))) {
                return "输入的16进制是错误的";
            }
        }
        // 16进制转化为10进制
        int x = Integer.valueOf(code, 16);
        // 将x转化为二进制
        String y = Integer.toBinaryString(x);
        StringBuffer sb = new StringBuffer();
        if (y.length() % 4 != 0) {
            // 把0补齐
            for (int i = 0; i < 4 - y.length() % 4; i++) {
                sb.append("0");
            }
        }
        sb.append(y);
        return sb.toString();
    }

    /**
     * 16进制加法
     *
     * @param hexdata
     * @return
     */
    public static String makeChecksum(String hexdata) {
        if (hexdata == null || hexdata.equals("")) {
            return "00";
        }
        hexdata = hexdata.replaceAll(" ", "");
        int total = 0;
        int len = hexdata.length();
        if (len % 2 != 0) {
            return "00";
        }
        int num = 0;
        while (num < len) {
            String s = hexdata.substring(num, num + 2);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        return Integer.toHexString(total).toUpperCase();
    }

    /**
     * 2进制转16进制
     *
     * @param code
     * @return
     */
    public static String binaryToHex(String code) {
        if (code.length() > 4) {
            int one = Integer.parseInt(code.substring(0, 4), 2);
            int two = Integer.parseInt(code.substring(4), 2);
            return Integer.toHexString(one).toUpperCase().concat(Integer.toHexString(two).toUpperCase());
        } else {
            // 将二进制转为十进制
            int result = Integer.parseInt(code, 2);
            // 将十进制转为十六进制
            return Integer.toHexString(result).toUpperCase();
        }
    }

}
