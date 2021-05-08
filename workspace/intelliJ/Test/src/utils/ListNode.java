package utils;

public class ListNode {
    public int val;
    public ListNode next;

    public ListNode() {
    }

    public ListNode(int x) {
        val = x;
    }

    public  static ListNode createLinkedList(int[] arr) {
        ListNode former = new ListNode(arr[0]);
        ListNode header = former;
        for (int i = 1; i < arr.length; i++) {
            ListNode node = new ListNode(arr[i]);
            former.next = node;
            former = node;
        }
        return header;
    }

    public  static void printLinkedList(ListNode root) {
        while (root != null) {
            System.out.print(root.val + ", ");
            root = root.next;
        }
    }
}
