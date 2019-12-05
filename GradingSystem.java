package jdbcAssignment;
import java.sql.*;
import java.util.Scanner;

public class GradingSystem {
	static{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws SQLException{
		String dburl = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			dburl = "jdbc:oracle:thin:@localhost:1521:xe";
			con = DriverManager.getConnection(dburl, "gradingsystem", "grade");
			
			Scanner in = new Scanner(System.in);
			int menu;
			boolean sw = true;
			String student_name;
			int student_id, korean_score, english_score, math_score;
			
			while(sw) {
				System.out.println("================");
				System.out.println("| 1. �����Է�          |");
				System.out.println("| 2. ��������          |");
				System.out.println("| 3. �̸�����          |");
				System.out.println("| 4. ��������          |");
				System.out.println("| 5. �����˻�          |");
				System.out.println("| 6. �������          |");
				System.out.println("| 7. ���α׷� ����   |");
				System.out.println("================");
				System.out.print("�޴��� �����ϼ���: ");
				
				menu = Integer.parseInt(in.nextLine());
				
				switch(menu) {
				case 1:
					System.out.print("�й��� �Է��ϼ���: ");
					student_id = Integer.parseInt(in.nextLine());
					
					System.out.print("�̸��� �Է��ϼ���: ");
					student_name = in.nextLine();
					
					System.out.print("������� �Է��ϼ���: ");
					korean_score = Integer.parseInt(in.nextLine());
					
					System.out.print("������� �Է��ϼ���: ");
					english_score = Integer.parseInt(in.nextLine());
					
					System.out.print("���м����� �Է��ϼ���: ");
					math_score = Integer.parseInt(in.nextLine());
					
					pstmt = con.prepareStatement("INSERT INTO SCORE VALUES(?, ?, ?, ?, ?)");
					
					pstmt.setInt(1, student_id);
					pstmt.setString(2, student_name);
					pstmt.setInt(3, korean_score);
					pstmt.setInt(4, english_score);
					pstmt.setInt(5, math_score);
					pstmt.executeUpdate();
					
					System.out.println("������ �ԷµǾ����ϴ�.");
					break;
					
				case 2:
					pstmt = con.prepareStatement("UPDATE SCORE SET korean_score = ?, english_score = ?, math_score = ? "
													+ "WHERE student_id = ?");
					
					System.out.print("������ ������ �л��� �й��� �Է��ϼ���: ");
					student_id = Integer.parseInt(in.nextLine());
					
					System.out.print("���ο� ������� �Է��ϼ���: ");
					korean_score = Integer.parseInt(in.nextLine());
					
					System.out.print("���ο� ������� �Է��ϼ���: ");
					english_score = Integer.parseInt(in.nextLine());
					
					System.out.print("���ο� ���м����� �Է��ϼ���: ");
					math_score = Integer.parseInt(in.nextLine());

					pstmt.setInt(1, korean_score);
					pstmt.setInt(2, english_score);
					pstmt.setInt(3, math_score);
					pstmt.setInt(4, student_id);
					pstmt.executeUpdate();
					
					System.out.println("������ �����Ǿ����ϴ�.");
					break;
					
				case 3:
					pstmt = con.prepareStatement("UPDATE SCORE SET student_name = ?"
													+ "WHERE student_id = ?");
					
					System.out.print("�̸��� ������ �л��� �й��� �Է��ϼ���: ");
					student_id = Integer.parseInt(in.nextLine());
					
					System.out.print("���ο� �̸��� �Է��ϼ���: ");
					student_name = in.nextLine();
					
					pstmt.setString(1, student_name);
					pstmt.setInt(2, student_id);
					pstmt.executeUpdate();
					
					System.out.println("�̸��� �����Ǿ����ϴ�.");
					break;
					
				case 4:
					pstmt = con.prepareStatement("DELETE FROM score WHERE student_id = ?");

					System.out.print("������ ������ �л��� �й��� �Է��ϼ���: ");
					student_id = Integer.parseInt(in.nextLine());

					pstmt.setInt(1, student_id);
					pstmt.executeUpdate();
					
					System.out.println("������ �����Ǿ����ϴ�.");
					break;
					
				case 5:
					pstmt = con.prepareStatement("SELECT * FROM score WHERE student_id = ?");
					
					System.out.print("������ �˻��� �л��� �й��� �Է��ϼ���: ");
					student_id = Integer.parseInt(in.nextLine());
					
					pstmt.setInt(1, student_id);
					rs = pstmt.executeQuery();
					
					System.out.println("�����" + "\t" + "�����" + "\t" + "���м���" + "\t");
					
					while(rs.next()) {
						System.out.print(rs.getInt("korean_score") + "\t");
						System.out.print(rs.getInt("english_score") + "\t");
						System.out.print(rs.getInt("math_score") + "\t");
						System.out.println();
					}
					break;
					
				case 6:
					pstmt = con.prepareStatement("SELECT student_id, student_name, korean_score, english_score, math_score, "
												+ "(korean_score + english_score + math_score)/3 AS aver, "
												+ "RANK() OVER (ORDER BY korean_score + english_score + math_score DESC) AS rank "
												+ "FROM score ORDER BY student_id");
					
					rs = pstmt.executeQuery();
					
					System.out.println("�й�" + "\t" + "\t" + "�̸�" + "\t" + "�����" 
									+ "\t" + "�����" + "\t" + "���м���" + "\t" + "���" + "\t" + "����" + "\t");
					
					while(rs.next()) {
						System.out.print(rs.getInt("student_id") + "\t");
						System.out.print(rs.getString("student_name") + "\t");
						System.out.print(rs.getInt("korean_score") + "\t");;
						System.out.print(rs.getInt("english_score") + "\t");
						System.out.print(rs.getInt("math_score") + "\t");
						System.out.print(rs.getInt("aver") + "\t");
						System.out.print(rs.getInt("rank") + "\t");
						System.out.println();
					}
					
					pstmt = con.prepareStatement("SELECT AVG(korean_score), AVG(english_score), "
													+ "AVG(math_score) FROM score");
					
					rs = pstmt.executeQuery();
					
					while(rs.next()) {
						if(rs.getInt("AVG(korean_score)") != 0 && rs.getInt("AVG(english_score)") != 0 
																	&& rs.getInt("AVG(math_score)") != 0) {
							System.out.print("<�� ���� �������>" + "\t" + "\t");
							System.out.print(rs.getInt("AVG(korean_score)") + "\t");
							System.out.print(rs.getInt("AVG(english_score)") + "\t");
							System.out.print(rs.getInt("AVG(math_score)") + "\t");
							System.out.println();
						}
					}
					break;
					
				case 7:
					sw = false;
					break;
				}
			}
		}catch(IllegalArgumentException e) {
			System.out.println("�Է� ���¸� Ȯ���ϼ���");
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			if(con != null) {try {con.close();}catch(Exception e) {}}
			if(pstmt != null) {try {pstmt.close();}catch(Exception e) {}}
			if(rs != null) {try {rs.close();}catch(Exception e) {}}
		}
	}
}
