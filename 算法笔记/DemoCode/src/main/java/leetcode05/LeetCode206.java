package leetcode05;

public class LeetCode206 {
    public ListNode reverseList(ListNode head) {
        if(head == null || head.next == null) return head;

        ListNode lastListNode = reverseList(head.next);
        head.next.next = head;
        head.next = null;

        return lastListNode;
    }

    public static void main(String[] args) {
        LeetCode206 leetCode206 = new LeetCode206();
        ListNode listNode = new ListNode(1);
        listNode.next = new ListNode(2);
        listNode.next.next = new ListNode(3);
        listNode.next.next.next = new ListNode(4);
        listNode.next.next.next.next = new ListNode(5);

        ListNode lastListNode = leetCode206.reverseList(listNode);
        ListNode.PrintList(lastListNode);
    }
}

class ListNode {
    int val;
    ListNode next;

    ListNode() {}

    ListNode(int val) { this.val = val; }

    ListNode(int val, ListNode next) { this.val = val; this.next = next; }

    public static void PrintList(ListNode listNode) {
        while (listNode != null) {
            System.out.print(listNode.val + " ");
            listNode = listNode.next;
        }
    }
}