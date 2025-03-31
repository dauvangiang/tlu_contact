import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import kotlin.concurrent.thread

fun main() {
    val serverSocket = ServerSocket(12345) // Cổng 12345
    println("Server đang chạy trên cổng 12345...")

    try {
        while (true) {
            val clientSocket = serverSocket.accept() // Chấp nhận kết nối từ client
            println("Đã kết nối với client: ${clientSocket.inetAddress.hostAddress}")

            // Tạo luồng mới để xử lý client
            thread {
                handleClient(clientSocket)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        serverSocket.close()
    }
}

fun handleClient(clientSocket: java.net.Socket) {
    try {
        val input = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        val output = PrintWriter(clientSocket.getOutputStream(), true)

        // Nhận và hiển thị tin nhắn từ client
        var message: String?
        while (input.readLine().also { message = it } != null) {
            println("Nhận từ client: $message")
            output.println("Server nhận được: $message") // Gửi phản hồi
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        clientSocket.close()
        println("Client đã ngắt kết nối")
    }
}